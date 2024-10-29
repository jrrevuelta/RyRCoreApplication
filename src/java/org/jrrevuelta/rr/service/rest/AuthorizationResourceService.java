package org.jrrevuelta.rr.service.rest;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jrrevuelta.rr.service.ejb.AuthorizationManager;
import org.jrrevuelta.rr.service.model.BearerToken;
import org.jrrevuelta.rr.service.model.Invitation;
import org.jrrevuelta.rr.service.model.User;

/** 
 * Authorization - This is the component that will handle all of the security interactions.
 * 
 * Through this resources, the Users are managed, and the session tokens are created.
 * 
 * @author José Ramón Revuelta
 */
public class AuthorizationResourceService implements AuthorizationResource {
	
	@EJB(name="ejb/RR-service/AuthorizationManager") private AuthorizationManager authorizationManager;

	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.rest");
	
	public AuthorizationResourceService() {
		super();
		log.finest("RR: AuthorizationResource instantiated (JAX-RS REST Resource)");
	}
	
	
	public Response invite(String subject, String role, String authorization) {
		
		log.fine("RR: Inviting: " + subject + ", as: " + role);
		log.fine("RR:     Authorization: " + authorization);
		
		ResponseBuilder builder = null;
		
		// Check Authorization
		User authority = authorizationManager.verifyToken(authorization);
		if (!authority.getRole().toString().matches("ADMIN|JUDGE|DELEGATE|COACH")) { 
			builder = Response.status(Response.Status.FORBIDDEN);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("User not allowed to issue an invitation.");
		}
		
		try {
			Invitation invitation = authorizationManager.invite(subject, role, authority);
			builder = Response.ok(invitation);
			log.fine("RR: Invitation generated and sent.");
			
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Internal server error.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Request could not be handled by the server.");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	
	}
	
	
	public Response signin(String invitationIdfr) {

		log.fine("RR: SignIn." + (invitationIdfr != null ? " with invitation: [" + invitationIdfr + "]" : ""));
		
		ResponseBuilder builder = null;
		
		try {
			Invitation invitation = authorizationManager.getInvitation(invitationIdfr);
			
			if (invitation.getStatus().equals(Invitation.InvitationLifeCycleStatus.CREATED)) {
				// TODO: Depending on the status of the invite, create the appropriate HTML (no invite, expired, used, normal)
			}
			
			String html = "<!DOCTYPE html>"   // TODO: Prepare the HTML in a cleaner, better way... from template files and CSS
					+ "<html>"
					+ "  <head>"
					+ "    <meta charset=\"UTF-8\">"
					+ "    <title>Bienvenido a Remo &amp; Regatas</title>"
					+ "  </head>"
					+ "  <body>"
					+ "    <h2>Remo &amp; Regatas</h2>"
					+ "    <h3>Registro de usuario nuevo</h3>"
					+ "    <form action=\"/Rowing/authorization/enroll\" method=\"post\">"
					+ "      <table>"
					+ "        <tr>"
					+ "          <td><label for=\"name\">Nombre</label></td>"
					+ "          <td><input name=\"name\" type=\"text\"" + (invitation.getName() != null ? " value =\"" + invitation.getName() + "\"" : "") + " required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"lastName\">Apellido</label></td>"
					+ "          <td><input name=\"lastName\" type=\"text\"" + (invitation.getLastName() != null ? " value=\"" + invitation.getLastName() + "\"" : "") + " required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"email\">e-mail</label></td>"
					+ "          <td><input name=\"email\" type=\"email\" value=\"" + invitation.getEmail() + "\" required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"phoneNumber\">Teléfono</label></td>"
					+ "          <td><input name=\"phoneNumber\" type=\"text\"/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"password\">Password</label></td>"
					+ "          <td><input name=\"password\" type=\"password\" required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"password2\">Verify Password</label></td>"
					+ "          <td><input name=\"password2\" type=\"password\" required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><td><button type=\"submit\">Submit</button></td></td>"
					+ "        </tr>"
					+ "      </table>"
					+ "      <input name=\"invitationIdfr\" type=\"hidden\" value=\"" + invitation.getIdfr() + "\"/>"
					+ "    </form>"
					+ "  </body>"
					+ "</html>";
			builder = Response.ok(html);
			
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Internal Exception generated.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Request could not be handled by the server");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	
	}
	
	
	public Response enroll(
			String name,
			String lastName,
			String email,
			String phoneNumber,
			String password,
			String password2,
			String invitationIdfr) {
		
		log.fine("RR: Enrolling new user: [" + name + " " + lastName + "]");
		
		ResponseBuilder builder = null;
		
		try {
			
			// TODO: Validate invitation and its status
			
			User newUser = new User();			
			newUser.setIdfr(email);
			newUser.setName(name);
			newUser.setLastName(lastName);
			newUser.setEmail(email);
			newUser.setPhoneNumber(phoneNumber);
			authorizationManager.createUser(newUser, invitationIdfr, password);
			
			// After sucessful creation of new user, let the user know its ready.
			builder = Response.ok("<html><head><meta charset=\"UTF-8\"/></head><body>"
					+ "OK: [ "
					+ "Name:" + name
					+ " - Lastname:" + lastName
					+ " - E-mail:" + email
					+ "- Phone Number:" + phoneNumber
					+ " - Password:" + password
					+ " - IDFR:" + invitationIdfr
					+ " ]"
					+ "</body></html>");
			
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Internal Exception generated.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Request could not be handled by the server");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	
	}
	
	
	public Response token(String username, String password) {
		
		log.fine("RR: Issuing token for [" + username + "]");
		
		ResponseBuilder builder = null;
		
		try {
			BearerToken token = authorizationManager.verifyUserCredentials(username, password);
			builder = Response.ok(token);
			
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Internal Exception generated.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Request could not be handled by the server");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build(); 
	}
}
