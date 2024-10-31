package org.jrrevuelta.rr.ejb;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jrrevuelta.rr.aws.AwsClientConfig;
import org.jrrevuelta.rr.aws.AwsSesSendEmail;
import org.jrrevuelta.rr.model.BearerToken;
import org.jrrevuelta.rr.model.Invitation;
import org.jrrevuelta.rr.model.User;
import org.jrrevuelta.rr.model.UserRole;
import org.jrrevuelta.rr.model.BearerToken.TokenLifeCycleStatus;
import org.jrrevuelta.rr.model.BearerToken.TokenType;
import org.jrrevuelta.rr.model.Invitation.InvitationLifeCycleStatus;
import org.jrrevuelta.security.passwords.SecuredPassword;
import org.jrrevuelta.security.passwords.SecuredPasswordGenerator;
import org.jrrevuelta.security.passwords.SecuredPasswordVerifier;


/**
 * 
 * @author José Ramón Revuelta
 */
@Stateless(name="ejb/RR-service/AuthorizationManager")
public class AuthorizationManagerBean implements AuthorizationManager {

	@PersistenceContext(name="RR-service") EntityManager em;
	@Resource(name="aws/RR-CloudServices", type=org.jrrevuelta.rr.aws.AwsClientConfig.class)
		private AwsClientConfig sesContext;
	private SecureRandom random = new SecureRandom();
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.ejb");
	
	
	public AuthorizationManagerBean() {
		super();
		log.finest("RR: AuthorizationManagerBean (EJB) instantiated.");
	}
	
	
	/////////////////////////////////////////////////////
	//// === CRUD Operations on User  ============== ////
	/////////////////////////////////////////////////////
	
	@Override
	public User createUser(User user, String invitationIdfr, String password) {
		log.fine("RR: Creating user [" + user.getIdfr() + ": " + user.getLastName() + ", " + user.getName() + "]");
		
		// TODO: Validate user's fields (should all be set correctly from calling method)
		
		// Validate invitation (assign role from invitation)
		Invitation invitation = getInvitation(invitationIdfr);   // TODO: Consider situations with the invitation status
		user.setRole(invitation.getRole());
		
		// Generate password's derived key to keep in DB
		SecuredPasswordGenerator generator = new SecuredPasswordGenerator();
		SecuredPassword securedPassword = generator.generateNewSecuredPassword(password);
		user.setPassword(securedPassword.getDerivedKey());
		user.setSalt(securedPassword.getSalt());
		user.setCounter(securedPassword.getCounter());
		
		// Create user in DB
		user.setStatus(User.UserLifeCycleStatus.ACTIVE);
		user.setTimestamp(new Date());
		em.persist(user);
		log.fine("RR: User #" + user.getId() + " created [" + user.getIdfr() + "]");
		
		return user;
	}
	

	@Override
	public User getUser(String idfr) {
		log.fine("RR: Getting User [" + idfr + "]");
		
		User user = null;
		
		try {
			TypedQuery<User> q = em.createNamedQuery("User.withIDFR", User.class);
			q.setParameter("idfr", idfr);
			user = q.getSingleResult();
			log.fine("RR: User found [" + user.getIdfr() + "]");
		} catch (NoResultException e) {
			log.fine("RR: EJB User not found [" + idfr + "].");
			//throw new RecordNotFoundException("Invitation not found: [" + idfr + "]");
		} catch (NonUniqueResultException e) {
			log.warning("RR: EJB More than one User found with IDFR: [" + idfr + "].");
			//throw new RecordErrorException("More than one Invitation found with IDFR: [" + idfr + "]");
		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting User [" + idfr + "].");
			//throw new SystemErrorException("Persistence error while getting Invitation [" + idfr + "].");
		}
		
		return user;
	}
	
	
	/////////////////////////////////////////////////////
	//// === Enrollment Functionality  ============= ////
	/////////////////////////////////////////////////////
	
	@Override
	public Invitation invite(String subject, String role, User authority) {
		log.fine("RR: Inviting " + subject + " as " + role);
		
		Invitation invitation = new Invitation();
		
		// Create a good random invitation IDFR (will expire in the established period)
		invitation.setIdfr(createRandomIdfr(8));
		
		// Get the name and email components out of the subject (mailto: format)
		parseInvitationEmail(subject, invitation);
		
		// Validate and set the role. in case of error, then use ROWER role...  TODO: Validate which authority can invite which role...
		if (role.toUpperCase().matches("ADMIN|JUDGE|DELEGATE|COACH|ROWER")) {
			invitation.setRole(UserRole.valueOf(role.toUpperCase()));
		} else {
			invitation.setRole(UserRole.ROWER);
		}
		
		invitation.setStatus(InvitationLifeCycleStatus.CREATED);
		invitation.setTimestamp(new Date());
		
		// Use AWS to actually send the email
		try {
			// TODO: Create the appropriate message template and values... using AWS templates
			AwsSesSendEmail emailWorker = new AwsSesSendEmail(this.sesContext);
			String protocol = "http";
			String server = "localhost:8080";  // TODO: Get these values from settings
			String path = "/Rowing/authorization/signin";
			String param = "invitation=" + invitation.getIdfr();
			URI link = new URI(protocol,server,path,param,null);
			String defaultName = "amigo del remo";
			String name = invitation.getName() != null ? invitation.getName() : defaultName;
			String authorityName = authority.getName() + " " + authority.getLastName();
			String values = "{\"name\":\"" + name + "\",\"link\":\"" + link + "\",\"authority\":\"" + authorityName + "\"}";
			emailWorker.sendTemplateEmail(invitation.getEmail(), "InvitacionJuezTemplate", values);
			log.fine("RR: Email sent to [" + invitation.getEmail() + "] with invitation [" + invitation.getIdfr() + "]");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		try {
			em.persist(invitation);
			log.fine("RR: Invitation #" + invitation.getId() + " created [" + invitation.getIdfr() + "]");
		} catch (Exception e) {
			log.warning("RR: JPA problem: " + e.getMessage());
		}
		
		return invitation;
	}
	
	
	@Override
	public Invitation getInvitation(String idfr) {
		log.fine("RR: Getting Invitation [" + idfr + "]");
		
		Invitation invitation = null;
		
		try {
			TypedQuery<Invitation> q = em.createNamedQuery("Invitation.withIDFR", Invitation.class);
			q.setParameter("idfr", idfr);
			invitation = q.getSingleResult();
			log.fine("RR: Invitation found [" + invitation.getIdfr() + "]");
		} catch (NoResultException e) {
			log.fine("RR: EJB Invitation not found [" + idfr + "].");
			//throw new RecordNotFoundException("Invitation not found: [" + idfr + "]");
		} catch (NonUniqueResultException e) {
			log.warning("RR: EJB More than one Invitation found with IDFR: [" + idfr + "].");
			//throw new RecordErrorException("More than one Invitation found with IDFR: [" + idfr + "]");
		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting Invitation [" + idfr + "].");
			//throw new SystemErrorException("Persistence error while getting Invitation [" + idfr + "].");
		}
		
		return invitation;
	}
	
	
	//////////////////////////////////////////////////////
	//// = Token Issuing & Verifying Functionality  = ////
	//////////////////////////////////////////////////////

	@Override
	public BearerToken verifyUserCredentials(String username, String password) {
		log.fine("RR: Verifying credentials for [" + username + "]");
		
		BearerToken token = null;
		User user = getUser(username);
		
		SecuredPassword storedPassword = new SecuredPassword(user.getPassword(), user.getSalt(), user.getCounter());
		SecuredPasswordVerifier verifier = new SecuredPasswordVerifier(storedPassword);
		
		if (verifier.verifyPassword(password)) {
			
			// Create a new token for this verified user
			token = new BearerToken();
			token.setAccessToken(createRandomIdfr(16));
			token.setTokenType(TokenType.Bearer);
			token.setExpiration(10 * 3600);   // JUDGE tokens have a life span of 10 hours (a day worth of a Regatta) -> TODO: use a function based on type of user or something...
			token.setTimestamp(new Date());
			token.setStatus(TokenLifeCycleStatus.ACTIVE);
			token.setUser(user);
			
			em.persist(token);
			log.fine("RR: User [" + user.getIdfr() + "] verified. Issuing token [" + token.getAccessToken() + "]");
		}
		
		return token;
	}
	
	
	@Override
	public BearerToken getToken(String idfr) {
		log.fine("RR: Getting Token [" + idfr + "]");
		
		BearerToken token = null;
		
		try {
			TypedQuery<BearerToken> q = em.createNamedQuery("Token.withAccessToken", BearerToken.class);
			q.setParameter("accessToken", idfr);
			token = q.getSingleResult();
			log.fine("RR: Token found [" + token.getAccessToken() + "]");
		} catch (NoResultException e) {
			log.fine("RR: EJB Token not found [" + idfr + "].");
			//throw new RecordNotFoundException("Token not found: [" + idfr + "]");
		} catch (NonUniqueResultException e) {
			log.warning("RR: EJB More than one Token found with IDFR: [" + idfr + "].");
			//throw new RecordErrorException("More than one Token found with IDFR: [" + idfr + "]");
		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting Token [" + idfr + "].");
			//throw new SystemErrorException("Persistence error while getting Token [" + idfr + "].");
		}
		
		return token;
	}
	
	
	@Override
	public User verifyToken(String tokenIdfr) {
		log.fine("RR: Verifying token...");
		
		User user = null;
		BearerToken token = getToken(tokenIdfr);
		if (token.isValid()) {
			user = token.getUser();
		}
		
		return user;
	}

	
	/////////////////////////////////////////////////////
	//// === Ancillary Functionality  ============== ////
	/////////////////////////////////////////////////////
	
	private void parseInvitationEmail(String subject,Invitation invitation) {
		
		if (subject.contains("<") && subject.contains(">")) {
			invitation.setEmail(subject.substring(subject.indexOf("<")+1, subject.indexOf(">")).trim());
			if (subject.contains(",")) {
				invitation.setLastName(subject.substring(0, subject.indexOf(",")).trim());
				invitation.setName(subject.substring(subject.indexOf(",")+1, subject.indexOf("<")).trim());
			} else {
				invitation.setName(subject.substring(0, subject.indexOf("<")).trim());
				if (invitation.getName().equals("")) invitation.setName(null);
			}
		} else {
			invitation.setEmail(subject);
		}
	}
	
	
	private String createRandomIdfr(int size) {
		
		byte[] idfrBytes = new byte[size];
		this.random.nextBytes(idfrBytes);
		return new BigInteger(1, idfrBytes).toString(16).toUpperCase();
	}

}
