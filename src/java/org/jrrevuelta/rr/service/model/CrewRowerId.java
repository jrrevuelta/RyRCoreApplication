package org.jrrevuelta.rr.service.model;

import java.io.Serializable;

public class CrewRowerId implements Serializable {
	
	private int crew;
	private int rower;
	
	private static final long serialVersionUID = 1L;
	
	public CrewRowerId() {
		super(); 
	}
	
	public CrewRowerId(int crew, int rower) {
		super();
		this.crew = crew;
		this.rower = rower;
	}
	
	
	public int getCrew() {
		return this.crew; 
	}
	
	public void setCrew(int crew) {
		this.crew = crew; 
	}
	
	public int getRower() {
		return this.rower; 
	}
	
	public void setRower(int rower) {
		this.rower = rower; 
	}
	
	public int hashCode() {
		return crew*10000 + rower;
	}
	
	public boolean equals(Object o) {
		if (o instanceof CrewRowerId) {
			CrewRowerId other = (CrewRowerId)o;
			if (this.crew == other.crew && this.rower == other.rower)
				return true;
		}
		return false; 
	}
	
}
