package org.jrrevuelta.rr.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jrrevuelta.rr.exceptions.TeamException;
import org.jrrevuelta.rr.model.Rower;
import org.jrrevuelta.rr.model.Team;
import org.jrrevuelta.rr.model.Rower.RowerLifeCycleStatus;

@Stateless(name="ejb/RR-service/TeamManager")
public class TeamManagerBean implements TeamManager {
	
	@PersistenceContext(name="RR-service") EntityManager em;
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.ejb");
	
	
	public TeamManagerBean() {
		super();
		log.finest("RR: TeamManagerBean (EJB) instantiated.");
	}
	

	/////////////////////////////////////////////////////
	//// ====== CRUD Operations on Team  =========== ////
	/////////////////////////////////////////////////////
	
	@Override
	public List<Team> getTeamsList(Date lastUpdate) throws TeamException {
		log.fine("RR: Getting Teams List (since last update).");
		
		List<Team> teams = null;
		
		try {
			TypedQuery<Team> q;
			if (lastUpdate != null) {
				q = em.createNamedQuery("Team.sinceLastUpdate", Team.class);
				q.setParameter("lastUpdate", lastUpdate);
			} else {
				q = em.createNamedQuery("Team.all", Team.class);
			}
			teams = q.getResultList();
			log.fine("RR: Got [" + teams.size() + "] teams.");

		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting Teams list: " + e.getMessage());
			throw new TeamException("Persistence error while getting Teams list.");
		}
		
		return teams;
	}
	
	
	@Override
	public Team getTeam(int teamId) throws TeamException {
		log.fine("RR: EJB Getting Team [" + teamId + "].");
		
		Team team;
		
		if (teamId <= 0) {
			log.fine("RR: EJB Team not found.");
			throw new TeamException("Team not found: [" + teamId + "]");
		}
		
		team = em.find(Team.class, teamId);
		if (team == null) {
			log.fine("RR: Team not found.");
			throw new TeamException("Team not found: [" + teamId + "]");
		}
		
		return team;
	}
	
	
	@Override
	public Team createTeam(Team team) throws TeamException {
		log.fine("RR: Creating team [" + team.getDisplayName() + "].");
		
		// TODO: Implement all exceptions handling (existing team, etc, and application creation exceptions)
		// TODO: Validate team integrity before inserting [team.validate()]
		team.setStatus(Team.TeamLifeCycleStatus.REGISTERED);
		
		// Create team in database
		em.persist(team);
		
		// TODO: Validate correct creation of team or else throw appropriate exception
		
		return team;
	}
	
	
	@Override
	public void setTeamLogo(int teamId, byte[] logoData) throws TeamException {
		log.fine("ADSS: Setting logo for Team [" + teamId + "]");
		
		Team team = getTeam(teamId);
		team.setLogo(logoData);   // TODO: Do some kind of validation about the uploaded image
		return;
	}
	
	
	@Override
	public void setTeamBlade(int teamId, byte[] bladeData) throws TeamException {
		log.fine("ADSS: Setting blade for Team [" + teamId + "]");
		
		Team team = getTeam(teamId);
		team.setBlade(bladeData);   // TODO: Do some kind of validation about the uploaded image
		return;
	}


	@Override
	public Rower createRower(int teamId, Rower rower) throws TeamException {
		log.fine("RR: Creating Rower [" + rower.getLastnameP() + ", " + rower.getName() + "] in team [" + teamId + "]");
		
		Team team = getTeam(teamId);
		rower.setTeam(team);
		rower.setStatus(RowerLifeCycleStatus.REGISTERED);
		em.persist(rower);
		
		return rower;
	}
	
}
