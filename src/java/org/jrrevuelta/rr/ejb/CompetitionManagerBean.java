package org.jrrevuelta.rr.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jrrevuelta.rr.exceptions.RegattaException;
import org.jrrevuelta.rr.model.CrewRower;
import org.jrrevuelta.rr.model.DisplayCrew;
import org.jrrevuelta.rr.model.DisplayRower;
import org.jrrevuelta.rr.model.Event;
import org.jrrevuelta.rr.model.Race;
import org.jrrevuelta.rr.model.RaceCrew;
import org.jrrevuelta.rr.model.Regatta;

@Stateless(name="ejb/RR-service/CompetitionManager")
public class CompetitionManagerBean implements CompetitionManager {
	
	@PersistenceContext(name="RR-service") EntityManager em;
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.ejb");
	
	
	public CompetitionManagerBean() {
		super();
		log.finest("RR: CompetitionManagerBean (EJB) instantiated.");
	}
	

	/////////////////////////////////////////////////////
	//// === CRUD Operations on Regatta  =========== ////
	/////////////////////////////////////////////////////
	
	@Override
	public List<Regatta> getRegattasList(Date lastUpdate) throws RegattaException {
		
		log.fine("RR: Getting Regattas List (since last update).");
		List<Regatta> regattas = null;
		
		try {
			TypedQuery<Regatta> q;
			if (lastUpdate != null) {
				q = em.createNamedQuery("Regatta.sinceLastUpdate", Regatta.class);
				q.setParameter("lastUpdate", lastUpdate);
			} else {
				q = em.createNamedQuery("Regatta.all", Regatta.class);
			}
			regattas = q.getResultList();
			log.fine("RR: Got [" + regattas.size() + "] regattas.");

		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting Regattas list: " + e.getMessage());
			throw new RegattaException("Persistence error while getting Regattas list.");
		}
		
		return regattas;
	}


	@Override
	public Regatta getRegatta(int id) throws RegattaException {
		log.fine("RR: EJB Getting Regatta [" + id + "].");
		Regatta regatta;
		
		if (id <= 0) {
			log.fine("RR: EJB Regatta not found.");
			throw new RegattaException("Regatta not found: [" + id + "]");
		}
		
		regatta = em.find(Regatta.class, id);
		if (regatta == null) {
			log.fine("RR: Regatta not found.");
			throw new RegattaException("Regatta not found: [" + id + "]");
		}
		
		return regatta;
	}


	@Override
	public Regatta createRegatta(Regatta regatta) throws RegattaException {
		log.fine("RR: Creating regatta [" + regatta.getDisplayName() + "].");
		
		// TODO: Implement all exceptions handling (existing regatta, etc, and application creation exceptions)
		// TODO: Validate regatta integrity before inserting [regatta.validate()]
		regatta.setStatus(Regatta.Status.INVITATION);
		//regatta.setLocationId(1);
		//regatta.setTimestamp(new Date());
		
		// Create regatta in database
		em.persist(regatta);
		
		// TODO: Validate correct creation of regatta or else throw appropriate exception
		
		return regatta;
	}
	

	@Override
	public void setRegattaLogo(int regattaId, byte[] logoData) throws RegattaException {
		log.fine("ADSS: Setting logo for Regatta [" + regattaId + "]");
		
		Regatta regatta = getRegatta(regattaId);
		regatta.setLogo(logoData);   // TODO: Do some kind of validation about the uploaded image
		return;
	}


	/////////////////////////////////////////////////////
	//// === CRUD Operations on Event  ============= ////
	/////////////////////////////////////////////////////

	@Override
	public List<Event> getEventsList(int regattaId, Date lastUpdate) throws RegattaException {
		
		log.fine("RR: Getting Events List (since last update).");
		log.fine("RR: regataId: [" + regattaId + "] - lastUpdate: [" + lastUpdate + "]");
		List<Event> events = null;
		
		try {
			Regatta regatta = getRegatta(regattaId);
			TypedQuery<Event> q;
			if (lastUpdate != null) {
				q = em.createNamedQuery("Event.forRegatta.sinceLastUpdate", Event.class);
				q.setParameter("regatta", regatta);
				q.setParameter("lastUpdate", lastUpdate);
			} else {
				q = em.createNamedQuery("Event.all.forRegatta", Event.class);
				q.setParameter("regatta", regatta);
			}
			events = q.getResultList();
			log.fine("RR: Got [" + events.size() + "] events for [" + regatta.getDisplayName() + "].");
			for (Event e: events) {
				if (e.getRaces() != null) {
					log.fine("RR: Event [" + e.getCategory().getIdfr() + e.getGender().getIdfr() + e.getBoat().getIdfr() + "] with " + e.getRaces().size() + " races.");
				} else {
					log.fine("RR: Event [" + e.getCategory().getIdfr() + e.getGender().getIdfr() + e.getBoat().getIdfr() + "] with no races.");
				}
				List<Race> races = e.getRaces();
				if (races != null) for (Race r: races) {
					log.fine("RR: Race [" + r.getRaceNumber() + "]: " + r.getProgression() + " - " + r.getStartTime());
				}
			}
		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting Events list: " + e.getMessage());
			throw new RegattaException("Persistence error while getting Events list.");
		}
		
		return events;
	}


	/////////////////////////////////////////////////////
	//// === CRUD Operations on Race  ============== ////
	/////////////////////////////////////////////////////

	@Override
	public Race getRace(int raceId) throws RegattaException {
		log.fine("RR: EJB Getting Race [" + raceId + "].");
		Race race;
		
		if (raceId <= 0) {
			log.fine("RR: EJB Race not found.");
			throw new RegattaException("Race not found: [" + raceId + "]");
		}
		
		race = em.find(Race.class, raceId);
		if (race == null) {
			log.fine("RR: Race not found.");
			throw new RegattaException("Race not found: [" + raceId + "]");
		}
		
		return race;
	}
	
	
	@Override
	public List<Race> getRacesList(int regattaId, Date lastUpdate) throws RegattaException {
		
		log.fine("RR: Getting Races List (since last update).");
		log.fine("RR: regataId: [" + regattaId + "] - lastUpdate: [" + lastUpdate + "]");
		List<Race> races = null;
		
		try {
			Regatta regatta = getRegatta(regattaId);
			TypedQuery<Race> q;
			if (lastUpdate != null) {
				q = em.createNamedQuery("Race.forRegatta.sinceLastUpdate", Race.class);
				q.setParameter("regatta", regatta);
				q.setParameter("lastUpdate", lastUpdate);
			} else {
				q = em.createNamedQuery("Race.all.forRegatta", Race.class);
				q.setParameter("regatta", regatta);
			}
			races = q.getResultList();
			log.fine("RR: Got [" + races.size() + "] races for [" + regatta.getDisplayName() + "].");
		} catch (PersistenceException e) {
			log.warning("RR: EJB Persistence error while getting Races list: " + e.getMessage());
			throw new RegattaException("Persistence error while getting Races list.");
		}
		
		return races;
	}


	@Override
	public List<DisplayCrew> getCrewsListForRace(int raceId) throws RegattaException {
		
		log.fine("RR: Getting Crews list for Race [" + raceId + "]");
		
		Race race = getRace(raceId);
				
		List<DisplayCrew> displayCrews = new ArrayList<DisplayCrew>();
		for (RaceCrew rc: race.getCrews()) {
			DisplayCrew dc = new DisplayCrew(rc.getCrew().getId(), rc.getLane(), rc.getCrew().getDisplayName(), rc.getCrew().isCombined());
			dc.setTeamId(rc.getCrew().getTeam().getId());
			List<DisplayRower> displayRowers = new ArrayList<DisplayRower>();
			for (CrewRower cr: rc.getCrew().getRowers()) {
				DisplayRower dr = new DisplayRower(cr.getRower().getName(), cr.getRower().getLastnameP(), cr.getSeat());
				displayRowers.add(dr);
			}
			dc.setRowers(displayRowers);
			displayCrews.add(dc);
		}
			
		return displayCrews;
	}

}
