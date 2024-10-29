package org.jrrevuelta.rr.service.model;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="Crew_Rower")
@IdClass(CrewRowerId.class)
@Access(AccessType.PROPERTY)
public class CrewRower implements Serializable {

	private Crew crew;
	private Rower rower;
	private String nominality;
	private String seat;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public CrewRower() {
		super();
		log.finest("RR: CrewRower (Core model bean - JAXB - JPA) instantiated.");
	}
	
	
	/**
	 * crew: Property that holds the crew that this rower belongs to. 
	 */
	@Id
	@ManyToOne()
	@JoinColumn(name="CrewId")
	public Crew getCrew() {
		return this.crew;
	}
	
	public void setCrew(Crew crew) {
		this.crew = crew;
	}
	
	
	/**
	 * rower: Property that holds the rower base entity that represents this rower in the crew. 
	 */
	@Id
	@ManyToOne()
	@JoinColumn(name="RowerId")
	public Rower getRower() {
		return this.rower;
	}
	
	public void setRower(Rower rower) {
		this.rower = rower;
	}
	
	
	/**
	 * nominality: Property that shows whether this rower is a seat-holder or a substitute. 
	 */
	@Column(name="Nominality")
	public String getNominality() {
		return this.nominality;
	}
	
	public void setNominality(String nominality) {
		this.nominality = nominality;
	}
	
	
	/**
	 * seat: Property that is used to show the seat that this rower occupies in the crew boat. 
	 */
	@Column(name="Seat")
	public String getSeat() {
		return this.seat;
	}
	
	public void setSeat(String seat) {
		this.seat = seat;
	}
	
}
