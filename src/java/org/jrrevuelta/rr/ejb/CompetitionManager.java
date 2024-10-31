package org.jrrevuelta.rr.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.jrrevuelta.rr.exceptions.RegattaException;
import org.jrrevuelta.rr.model.DisplayCrew;
import org.jrrevuelta.rr.model.Event;
import org.jrrevuelta.rr.model.Race;
import org.jrrevuelta.rr.model.Regatta;

@Local
public interface CompetitionManager {
	
	// CRUD operations on Regatta
	public List<Regatta> getRegattasList(Date lastUpdate) throws RegattaException;
	public Regatta getRegatta(int regattaId) throws RegattaException;
	public Regatta createRegatta(Regatta regatta) throws RegattaException;
	public void setRegattaLogo(int regattaId, byte[] logo) throws RegattaException;
	
	// CRUD operations on Event
	public List<Event> getEventsList(int regattaId, Date lastUpdate) throws RegattaException;
	
	// CRUD operations on Race
	public Race getRace(int raceId) throws RegattaException;
	public List<Race> getRacesList(int regattaId, Date lastUpdate) throws RegattaException;
	public List<DisplayCrew> getCrewsListForRace(int raceId) throws RegattaException;
}
