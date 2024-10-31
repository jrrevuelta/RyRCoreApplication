package org.jrrevuelta.rr.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jrrevuelta.rr.ejb.CompetitionManager;
import org.jrrevuelta.rr.exceptions.RegattaException;
import org.jrrevuelta.rr.model.CrewRegistration;
import org.jrrevuelta.rr.model.DisplayCrew;
import org.jrrevuelta.rr.model.Event;
import org.jrrevuelta.rr.model.Race;
import org.jrrevuelta.rr.model.Regatta;

/** 
 * Competition - Everything surrounding a single regatta; all of its events, races (through the 
 * progression), crews (created for each regatta), and times & results.
 * 
 * @author José Ramón Revuelta
 */
public class CompetitionResourceService implements CompetitionResource {
	
	@EJB(name="ejb/RR-service/CompetitionManager") private CompetitionManager competitionManager;
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.rest");
	
	public CompetitionResourceService() {
		super();
		log.finest("RR: CompetitionResource instantiated (JAX-RS REST Resource)");
	}
	
	
	@Override
	public Response getRegattasList(String sinceParam) {
		log.fine("RR: Getting Regattas list.");
		
		ResponseBuilder builder = null;
		
		try {
			List<Regatta> regattas = null;
			Date sinceDate = null;
			if (sinceParam != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"); 
				sinceDate = dateFormat.parse(sinceParam);
			}
			regattas = competitionManager.getRegattasList(sinceDate);
			if (regattas.isEmpty()) {
				builder = Response.notModified();
				log.fine("RR: Regattas list not modified since last update.");
			} else {
				GenericEntity<List<Regatta>> regattasListEntity = new GenericEntity<List<Regatta>>(regattas){};
				builder = Response.ok(regattasListEntity);
				log.fine("RR: Got regattas list with [" + regattas.size() + "] elements.");
			}
		} catch (ParseException e) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Invalid date: [" + sinceParam + "].");
			log.warning("RR: Invalid last update date. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Invalid date: [" + sinceParam + "].");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}
	
	
	@Override
	public Response createRegatta(Regatta regatta) {
		log.fine("RR: Creating Regatta [" + regatta.getDisplayName() + "].");
		
		ResponseBuilder builder = null;
		
		try {
			Regatta createdRegatta = competitionManager.createRegatta(regatta);
			builder = Response.ok(createdRegatta);
			builder.status(Response.Status.CREATED);
			CacheControl cc = new CacheControl();
			cc.setNoCache(true);
			builder.cacheControl(cc);
			log.fine("RR: Created regatta [" + createdRegatta.getDisplayName()
					 + "(#" + createdRegatta.getId() + "): " + createdRegatta.getName() + "]");
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}
	
	
	@Override
	public Response loadRegattaLogo(int regattaId, byte[] logoData) {
		log.fine("RR: Loading logo for regatta: [" + regattaId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			competitionManager.setRegattaLogo(regattaId, logoData);
			builder = Response.accepted();
			log.fine("RR: Updated logo for Regatta [" + regattaId + "]");
			
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}

	
	@Override
	public Response getRegattaLogo(int regattaId) {
		log.fine("RR: Getting logo for Regatta [" + regattaId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			Regatta regatta = competitionManager.getRegatta(regattaId);
			builder = Response.ok(regatta.getLogo());
			log.fine("RR: Logo for Regatta [" + regattaId + "]");
			
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}
	
	
	@Override
	public Response getEventsList(int regattaId, String sinceParam) {
		log.fine("RR: Getting Events list.");
		
		ResponseBuilder builder = null;
		
		try {
			List<Event> events = null;
			Date sinceDate = null;
			if (sinceParam != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sinceDate = dateFormat.parse(sinceParam);
			}
			events = competitionManager.getEventsList(regattaId, sinceDate);
			if (events.isEmpty()) {
				builder = Response.notModified();
				log.fine("RR: Events list not modified since last update.");
			} else {
				GenericEntity<List<Event>> eventsListEntity = new GenericEntity<List<Event>>(events){};
				builder = Response.ok(eventsListEntity);
				log.fine("RR: Got events list with [" + events.size() + "] elements.");
			}
		} catch (ParseException e) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Invalid date: [" + sinceParam + "].");
			log.warning("RR: Invalid last update date. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Invalid date: [" + sinceParam + "].");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}


	@Override
	public Response createEvent(Event event) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	@Override
	public Response crewRegistration(CrewRegistration registration) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	@Override
	public Response getRacesList(int regattaId, String sinceParam) {
		log.fine("RR: Getting Races list.");
		
		ResponseBuilder builder = null;
		
		try {
			List<Race> races = null;
			Date sinceDate = null;
			if (sinceParam != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sinceDate = dateFormat.parse(sinceParam);
			}
			races = competitionManager.getRacesList(regattaId, sinceDate);
			if (races.isEmpty()) {
				builder = Response.notModified();
				log.fine("RR: Races list not modified since last update.");
			} else {
				GenericEntity<List<Race>> racesListEntity = new GenericEntity<List<Race>>(races){};
				builder = Response.ok(racesListEntity);
				log.fine("RR: Got races list with [" + races.size() + "] elements.");
			}
		} catch (ParseException e) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Invalid date: [" + sinceParam + "].");
			log.warning("RR: Invalid last update date. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Invalid date: [" + sinceParam + "].");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}


	@Override
	public Response getCrewsListForRace(int raceId) {
		log.fine("RR: Getting Crews list for race [" + raceId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			List<DisplayCrew> crews = null;
			crews = competitionManager.getCrewsListForRace(raceId);
			if (crews.isEmpty()) {
				builder = Response.notModified();
				log.fine("RR: Races list not modified since last update.");
			} else {
				GenericEntity<List<DisplayCrew>> crewsListEntity = new GenericEntity<List<DisplayCrew>>(crews){};
				builder = Response.ok(crewsListEntity);
				log.fine("RR: Got crews list with [" + crews.size() + "] elements.");
			}
		} catch (RegattaException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Regatta Exception catched.");
			log.warning("RR: EJB Regatta Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (EJBException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("EJB Exception catched.");
			log.warning("RR: EJB Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Other Exception catched.");
			log.warning("RR: Other Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
		} finally {
			if (builder == null) {
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
				builder.type(MediaType.TEXT_PLAIN_TYPE);
				builder.entity("Internal Error.");
				log.warning("RR: No Exception catched, but there was an unidentified problem in creating the response.");
			}
		}
		
		return builder.build();
	}
	
}
