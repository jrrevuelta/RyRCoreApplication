package org.jrrevuelta.rr.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jrrevuelta.rr.model.Team;

@Path("/team")
@Produces("application/xml")
@Consumes("application/xml")
public interface TeamResource {
	
	@GET
	@Path("/teams")
	public Response getTeamsList(
			@QueryParam("since") String sinceParam);
	
	@POST
	@Path("/team")
	public Response createTeam(Team team);
	
	@PUT
	@Path("/{teamId}/logo")
	@Consumes("image/png")
	public Response loadTeamLogo(
			@PathParam("teamId") int teamId,
			byte[] logoData);
	
	@GET
	@Path("/{teamId}/logo")
	@Produces("image/png")
	public Response getTeamLogo(
			@PathParam("teamId") int teamId);
	
	@PUT
	@Path("/{teamId}/blade")
	@Consumes("image/png")
	public Response loadTeamBlade(
			@PathParam("teamId") int teamId,
			byte[] bladeData);
	
	@GET
	@Path("/{teamId}/blade")
	@Produces("image/png")
	public Response getTeamBlade(
			@PathParam("teamId") int teamId);
/*	
	@GET
	@Path("/{teamId}/rowers")
	public Response getRowersList(
			@PathParam("teamId") int teamId,
			@QueryParam("since") String sinceParam);
	
	@POST
	@Path("/{teamId}/rower")
	public Response createRower(
			@PathParam("teamId") int teamId,
			Rower rower);
*/	
	@POST
	@Path("/{teamId}/rower")
	@Consumes("application/x-www-form-urlencoded")
	public Response createRower(
			@PathParam("teamId") int teamId,
			@FormParam("name") String name,
			@FormParam("lastnameP") String lastnameP,
			@FormParam("lastnameM") String lastnameM);
	
	@GET
	@Path("/addRower")
	@Produces("text/html")
	public Response selectTeam();
	
	@GET
	@Path("/{teamId}/addRower")
	@Produces("text/html")
	public Response addRower(
			@PathParam("teamId") int teamId);
}
