package org.jrrevuelta.rr.model;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CrewRower: Core class of the Data Model for the R&R Service.
 * 
 * A CrewRower represents a rower in a particular Crew. It relates to the Crew and to the Rower
 * that are being linked together. This is a Relationship entity (with some attributes, hence it 
 * has to be uni-directional).
 * 
 * A crew consolidates the rowers in the boat, and sets their nominality (whether they are substitutes
 * or definite rowers) in the crew, and the seat they occupy in the boat including the coxwain. 
 * It doesn't matter if the rowers are from the same team or combined.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Race_Crew")
@IdClass(RaceCrewId.class)
@Access(AccessType.PROPERTY)
public class RaceCrew implements Serializable {

	private Race race;
	private Crew crew;
	private int lane;
	//private String status;  // for: DNS, DNF, DSQ, EXC...
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public RaceCrew() {
		super();
		log.finest("RR: RaceCrew (Core model bean - JAXB - JPA) instantiated.");
	}
	
	
	/**
	 * race: Property that ... 
	 */
	@Id
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RaceId")
	public Race getRace() {
		return this.race;
	}
	
	public void setRace(Race race) {
		this.race = race;
	}
	
	
	/**
	 * crew: Property that holds the crew that ... 
	 */
	@Id
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CrewId")
	public Crew getCrew() {
		return this.crew;
	}
	
	public void setCrew(Crew crew) {
		this.crew = crew;
	}
	
	
	/**
	 * lane: Property that ... 
	 */
	@Column(name="Lane")
	public int getLane() {
		return this.lane;
	}
	
	public void setLane(int lane) {
		this.lane = lane;
	}
	
}
