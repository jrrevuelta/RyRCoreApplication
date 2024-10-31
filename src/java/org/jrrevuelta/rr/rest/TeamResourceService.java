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

import org.jrrevuelta.rr.ejb.TeamManager;
import org.jrrevuelta.rr.exceptions.TeamException;
import org.jrrevuelta.rr.model.Rower;
import org.jrrevuelta.rr.model.Team;

/** 
 * Team - Everything surrounding each team; all of its rowers, logos and blade colors... 
 * 
 * @author José Ramón Revuelta
 */
public class TeamResourceService implements TeamResource{
	
	@EJB(name="ejb/RR-service/TeamManager") private TeamManager teamManager;
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.rest");
	
	public TeamResourceService() {
		super();
		log.finest("RR: TeamResource instantiated (JAX-RS REST Resource)");
	}
	

	@Override
	public Response getTeamsList(String sinceParam) {
		log.fine("RR: Getting Teams list.");
		
		ResponseBuilder builder = null;
		
		try {
			List<Team> teams = null;
			Date sinceDate = null;
			if (sinceParam != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"); 
				sinceDate = dateFormat.parse(sinceParam);
			}
			teams = teamManager.getTeamsList(sinceDate);
			if (teams.isEmpty()) {
				builder = Response.notModified();
				log.fine("RR: Teams list not modified since last update.");
			} else {
				GenericEntity<List<Team>> teamsListEntity = new GenericEntity<List<Team>>(teams){};
				builder = Response.ok(teamsListEntity);
				log.fine("RR: Got teams list with [" + teams.size() + "] elements.");
			}
		} catch (ParseException e) {
			builder = Response.status(Response.Status.BAD_REQUEST);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Invalid date: [" + sinceParam + "].");
			log.warning("RR: Invalid last update date. [" + e.getClass() + ": " + e.getMessage() + "]");
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response createTeam(Team team) {
		log.fine("RR: Creating Team [" + team.getDisplayName() + "].");
		
		ResponseBuilder builder = null;
		
		try {
			Team createdTeam = teamManager.createTeam(team);
			builder = Response.ok(createdTeam);
			builder.status(Response.Status.CREATED);
			CacheControl cc = new CacheControl();
			cc.setNoCache(true);
			builder.cacheControl(cc);
			log.fine("RR: Created team [" + createdTeam.getDisplayName()
					 + "(#" + createdTeam.getId() + "): " + createdTeam.getName() + "]");
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response loadTeamLogo(int teamId, byte[] logoData) {
		log.fine("RR: Loading logo for team: [" + teamId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			teamManager.setTeamLogo(teamId, logoData);
			builder = Response.accepted();
			log.fine("RR: Updated logo for Team [" + teamId + "]");
			
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response getTeamLogo(int teamId) {
		log.fine("RR: Getting logo for Team [" + teamId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			Team team = teamManager.getTeam(teamId);
			builder = Response.ok(team.getLogo());
			log.fine("RR: Logo for Team [" + teamId + "]");
			
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response loadTeamBlade(int teamId, byte[] bladeData) {
		log.fine("RR: Loading blade for team: [" + teamId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			teamManager.setTeamBlade(teamId, bladeData);
			builder = Response.accepted();
			log.fine("RR: Updated blade for Team [" + teamId + "]");
			
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response getTeamBlade(int teamId) {
		log.fine("RR: Getting blade for Team [" + teamId + "]");
		
		ResponseBuilder builder = null;
		
		try {
			Team team = teamManager.getTeam(teamId);
			builder = Response.ok(team.getBlade());
			log.fine("RR: Blade for Team [" + teamId + "]");
			
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response createRower(int teamId, String name, String lastnameP, String lastnameM) {
		log.fine("RR: Creating Rower [" + name + " " + lastnameP + "] for team [" + teamId +"]");
		
		ResponseBuilder builder = null;
		
		try {
			Rower newRower = new Rower();
			newRower.setName(name);
			newRower.setLastnameP(lastnameP);
			newRower.setLastnameM(lastnameM);
			Rower createdRower = teamManager.createRower(teamId, newRower);
			builder = Response.ok(createdRower);
			builder.status(Response.Status.CREATED);
			log.fine("RR: Created Rower [" + createdRower.getId() + "]");
			
		} catch (TeamException e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.type(MediaType.TEXT_PLAIN_TYPE);
			builder.entity("Team Exception catched.");
			log.warning("RR: EJB Team Exception catched. [" + e.getClass() + ": " + e.getMessage() + "]");
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
	public Response addRower(int teamId) {
		log.fine("RR: Add Rower.");
		
		ResponseBuilder builder = null;
		
		try {
			
			Team team = teamManager.getTeam(teamId);
			
			String html = "<!DOCTYPE html>"
					+ "<html>"
					+ "  <head>"
					+ "    <meta charset=\"UTF-8\">"
					+ "    <title>Agregar un remero</title>"
					+ "  </head>"
					+ "  <body>"
					+ "    <h2>Remo &amp; Regatas</h2>"
					+ "    <h3>Registro de remero nuevo para el equipo: " + team.getDisplayName() + "</h3>"
					+ "    <form action=\"/rr/team/" + team.getId() + "/rower\" method=\"post\">"
					+ "      <table>"
					+ "        <tr>"
					+ "          <td><label for=\"name\">Nombre</label></td>"
					+ "          <td><input name=\"name\" type=\"text\" required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"lastnameP\">Apellido Paterno</label></td>"
					+ "          <td><input name=\"lastnameP\" type=\"text\" required/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><label for=\"lastnameM\">Apellido Materno</label></td>"
					+ "          <td><input name=\"lastnameM\" type=\"text\"/></td>"
					+ "        </tr>"
					+ "        <tr>"
					+ "          <td><td><button type=\"submit\">Submit</button></td></td>"
					+ "        </tr>"
					+ "      </table>"
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


	@Override
	public Response selectTeam() {
		log.fine("RR: Select team.");
		
		ResponseBuilder builder = null;
		
		try {
			
			List<Team> teams = teamManager.getTeamsList(null);
			
			String html = "<!DOCTYPE html>"
					+ "<html>"
					+ "  <head>"
					+ "    <meta charset=\"UTF-8\">"
					+ "    <title>Seleccionar equipo</title>"
					+ "  </head>"
					+ "  <body>"
					+ "    <h2>Remo &amp; Regatas</h2>"
					+ "    <h3>Agregar Remeros</h3>"
					+ "    <h3>Selecciona un equipo</h3>"
					+ "    <ul>";
			
			for (Team t: teams) {
				html += "<li><a href=\"/rr/team/" + t.getId() + "/addRower\">" + t.getDisplayName() + "</a> - " + t.getName() + "</li>";
			}
					
			html	+="    </ul>"
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

}
