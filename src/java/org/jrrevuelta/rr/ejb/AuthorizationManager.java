package org.jrrevuelta.rr.ejb;

import javax.ejb.Local;

import org.jrrevuelta.rr.model.BearerToken;
import org.jrrevuelta.rr.model.Invitation;
import org.jrrevuelta.rr.model.User;

@Local
public interface AuthorizationManager {
	
	// CRUD operations on User
	public User getUser(String idfr);
	public User createUser(User user, String invitationIdfr, String password);
	
	// Functionality for the enrollment process
	public Invitation getInvitation(String idfr);
	public Invitation invite(String subject, String role, User authority);
	
	// Functionality for token issuing and validation process
	public BearerToken getToken(String tokenIdfr);
	public BearerToken verifyUserCredentials(String username, String password);
	public User verifyToken(String tokenIdfr);
}
