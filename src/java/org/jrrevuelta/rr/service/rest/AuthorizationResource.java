package org.jrrevuelta.rr.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/** 
 * Authorization - This is the component that will handle all of the security interactions with
 * the outside world.<br/>
 * <br/>
 * Through this resource; the Users are created, enrolled and managed, and the session tokens are 
 * dispatched to the client applications.<br/>
 * <br/>
 * @author José Ramón Revuelta
 */
@Path("/authorization")
@Produces("application/xml")
@Consumes("application/x-www-form-urlencoded")
public interface AuthorizationResource {
	
	/**
	 * Invite: Send an invitation email to enroll in the system (and create a new User).<br/>
	 * <br/>
	 * This method will be invoked from the enabled devices with the R&R app. It is not expected 
	 * to be called from the wild, and requires a valid Bearer Token.<br/>
	 * <br/>
	 * The subject MUST include at least the e-mail to contact the invitee. It MAY contain the
	 * name and lastname in advance in "mailto:" format.<br/>
	 * e.g. Revuelta Lazcano, José Ramón &lt;j.r.revuelta@computer.org&gt;<br/>
	 * <br/>
	 * While provisioning the invitation, the role MAY be established (ROWER, COACH, DELEGATE, 
	 * JUDGE, ADMIN). If no role is authorized, the ROWER status is given.
	 * <br/>
	 * The e-mail is directly sent to the invitee with an appropriate text and a link to the
	 * enroll process including the 'InvitationIdfr' and the expiration period. 
	 * <br/>
	 * @param subject Email of the invitee in "mailto:" format, i.e. it may contain the full name 
	 * of the subject: e.g. Revuelta Lazcano, José Ramón &lt;j.r.revuelta@computer.org&gt;
	 * @param role The authorized role for the subject. It may be one of: ROWER, COACH, DELEGATE, 
	 * JUDGE, ADMIN, case insensitive.
	 * @param authorization Bearer Token included in the 'Authorization' HTTP Header as defined in [RFC6750]
	 * @return After successfully sending the e-mail, a 200 HTTP return code is issued and the body contains 
	 * the XML representation of the provisioned 'Invitation'.<br/>
	 * May return 400 Bad Request, 401 Authorization Required , 403 No Privilege
	 */
	@POST
	@Path("/invite")
	public Response invite(
			@FormParam("subject") String subject,
			@FormParam("role") String role,
			@HeaderParam("Authorization") String authorization);
	
	/**
	 * Sign In: Presents an HTML form with the info for enrollment, and initiates the process.
	 * 
	 * This method is the landing point of the link sent with the invitations by e-mail, and 
	 * requires the 'InvitationIdfr' previously provisioned (see: Invite)
	 * 
	 * MUST receive a valid invitation to prevent anonymous enrollment.
	 */
	@GET
	@Path("/signin")
	@Produces("text/html")
	public Response signin(
			@QueryParam("invitation") String invitation);
	
	/**
	 * Enroll: Gets the information given by the new user in the web form to get enrolled and 
	 * creates a new user in the system.
	 * 
	 * MUST receive a valid invitation to prevent anonymous enrollment.
	 */
	@POST
	@Path("/enroll")
	@Produces("text/html")
	public Response enroll(
			@FormParam("name") String name,
			@FormParam("lastName") String lastName,
			@FormParam("email") String email,
			@FormParam("phoneNumber") String phoneNumber,
			@FormParam("password") String password,
			@FormParam("password2") String password2,
			@FormParam("invitationIdfr") String invitationIdfr);
	
	/**
	 * Token: Validates user's credentials and issues a Bearer Token to be used throughout the
	 * system to make valid authenticated calls.
	 * 
	 * This is the ONLY method that should be called using the user's credentials. Its functionality
	 * is based on the "Resource Owner Credentials" model of the OAuth 2.0 specification [RFC6749]
	 * with very slight modifications (since this implementation doesn't handle more generic cases).
	 * In the Request; 'grant_type' parameter is not needed, since it is always 'password', and 
	 * 'scope' parameter is decided internally by this system.
	 * In the Response; There is no 'refesh_token' in this model, and, as in the request no 'scope'.
	 * 
	 *  Since this method may be called in the wild, it will be throttled to avoid on-line brute 
	 *  force attacks.
	 *  
	 *  @param username The unique identifier of each user, in this case, their email address.
	 *  @param password The secret password that was decided by each user.
	 *  @return After successful validation of the user's credentials, a 'Bearer Token' object will
	 *  be issued to be used by clients as described in [RFC6750] in all methods of this service
	 *  that require authentication.
	 */
	@POST
	@Path("/token")
	public Response token(
			@FormParam("username") String username,
			@FormParam("password") String password);
	
}
