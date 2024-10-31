package org.jrrevuelta.rr.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jrrevuelta.rr.model.CrewRegistration;
import org.jrrevuelta.rr.model.Event;
import org.jrrevuelta.rr.model.Regatta;

@Path("/competition")
@Produces("application/xml")
@Consumes("application/xml")
public interface CompetitionResource {

	@GET
	@Path("/regattas")
	public Response getRegattasList(
			@QueryParam("since") String sinceParam);
	
	@POST
	@Path("/regatta")
	public Response createRegatta(Regatta regatta);
	
	@PUT
	@Path("/{regattaId}/logo")
	@Consumes("image/png")
	public Response loadRegattaLogo(
			@PathParam("regattaId") int regattaId,
			byte[] logoData);
	
	@GET
	@Path("/{regattaId}/logo")
	@Produces("image/png")
	public Response getRegattaLogo(
			@PathParam("regattaId") int regattaId);
	
	@GET
	@Path("/{regattaId}/events")
	public Response getEventsList(
			@PathParam("regattaId") int regattaId,
			@QueryParam("since") String sinceParam);
	
	@POST
	@Path("/{regattaId}/event")
	public Response createEvent(Event event);
	
	@POST
	@Path("/{regattaId}/{eventId}/crewRegistration")
	public Response crewRegistration(CrewRegistration registration);
	
	@GET
	@Path("/{regattaId}/races")
	public Response getRacesList(
			@PathParam("regattaId") int regattaId,
			@QueryParam("since") String sinceParam);
	
	@GET
	@Path("/{raceId}/crews")
	public Response getCrewsListForRace(
			@PathParam("raceId") int raceId);
}
