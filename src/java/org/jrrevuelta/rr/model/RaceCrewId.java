package org.jrrevuelta.rr.model;

import java.io.Serializable;

public class RaceCrewId implements Serializable {
	
	private int race;
	private int crew;
	
	private static final long serialVersionUID = 1L;
	
	public RaceCrewId() {
		super(); 
	}
	
	public RaceCrewId(int race, int crew) {
		super();
		this.race = race;
		this.crew = crew;
	}
	
	
	public int getRace() {
		return this.race;
	}
	
	public void setRace(int race) {
		this.race = race;
	}
	
	public int getCrew() {
		return this.crew; 
	}
	
	public void setCrew(int crew) {
		this.crew = crew; 
	}
	
	public int hashCode() {
		return race*100000 + crew;
	}
	
	public boolean equals(Object o) {
		if (o instanceof RaceCrewId) {
			RaceCrewId other = (RaceCrewId)o;
			if (this.crew == other.crew && this.race == other.race)
				return true;
		}
		return false; 
	}
	
}
