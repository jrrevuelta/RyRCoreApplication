package org.jrrevuelta.rr.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.jrrevuelta.rr.exceptions.TeamException;
import org.jrrevuelta.rr.model.Rower;
import org.jrrevuelta.rr.model.Team;

@Local
public interface TeamManager {
	
	// CRUD operations on Team
	public List<Team> getTeamsList(Date lastUpdate) throws TeamException;
	public Team getTeam(int teamId) throws TeamException;
	public Team createTeam(Team team) throws TeamException;
	public void setTeamLogo(int teamId, byte[] logoData) throws TeamException;
	public void setTeamBlade(int teamId, byte[] bladeData) throws TeamException;
	
	// CRUD operations on Rower
	public Rower createRower(int teamId, Rower rower) throws TeamException;
	
}
