package org.jrrevuelta.rr.service.ejb;

import javax.ejb.Local;

import org.jrrevuelta.rr.service.model.BearerToken;
import org.jrrevuelta.rr.service.model.Invitation;
import org.jrrevuelta.rr.service.model.User;

@Local
public interface AuthorizationManager {
	
	// CRUD operations on User
	public User createUser(User user, String invitationIdfr, String password);
	public User getUser(String idfr);
	
	// Functionality for the enrollment process
	public Invitation invite(String subject, String role, User authority);
	public Invitation getInvitation(String idfr);
	
	// Functionality for token issuing and validation process
	public BearerToken verifyUserCredentials(String username, String password);
	public BearerToken getToken(String tokenIdfr);
	public User verifyToken(String tokenIdfr);
}
