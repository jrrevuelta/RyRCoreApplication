package org.jrrevuelta.rr.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Crew: Core class of the Data Model for the R&R Service.
 * 
 * A Crew represents a team's assembly of rowers to compete on a given event.
 * 
 * A crew consolidates the teams (if combined) and the rowers involved, it is given a name based
 * on the team it belongs keeping a differentiation in case more than one crew of a team competes
 * in a single event, also the combination of names from combined teams.
 * 
 * Always one team is "responsible" for a crew (if combined), and it is the one that handles the 
 * registration. 
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Crew")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Crew.all.forEvent",
            query="SELECT c FROM Crew c WHERE c.event = :event"),
	@NamedQuery(name="Crew.all.forTeam",
            query="SELECT c FROM Crew c WHERE c.team = :team"),
	@NamedQuery(name="Crew.forEvent.sinceLastUpdate",
            query="SELECT c FROM Crew c WHERE c.event = :event AND c.timestamp > :lastUpdate"),
	@NamedQuery(name="Crew.forTeam.sinceLastUpdate",
            query="SELECT c FROM Crew c WHERE c.team = :team AND c.timestamp > :lastUpdate")
})
@XmlRootElement(name="crew")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Crew implements Serializable {
	
	private int id;
	private String displayName;
	private boolean combined = false;
	private Event event;
	private Team team;
	private List<CrewRower> rowers;
	//private List<RaceCrew> races;
	private Date timestamp;
	private CrewLifeCycleStatus status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public Crew() {
		super();
		log.finest("RR: Crew (Core model bean - JAXB - JPA) instantiated.");
	}
	
	// Life-cycle status 
	public enum CrewLifeCycleStatus {
		REGISTERED,
		CANCELED
	}
	
	
	/**
	 * id: Property with the internal identity of this entity
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	@XmlAttribute
	public int getId() {
		return this.id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * displayName: Property that is used to present the name of the crew in "short" contexts. 
	 */
	@Column(name="DisplayName")
	@XmlElement(name="displayName")
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
	/**
	 * combination: Property that indicates whether this crew is a combination from more than one team. 
	 */
	@Column(name="Combined")
	@XmlElement(name="combined")
	public boolean isCombined() {
		return this.combined;
	}
	
	public void setCombined(boolean combined) {
		this.combined = combined;
	}
	
	
	/**
	 * event: Property that holds the event that this crew belongs to. 
	 */
	@ManyToOne()
	@JoinColumn(name="EventId")
	@XmlTransient
	public Event getEvent() {
		return this.event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
	/**
	 * team: Property that holds the team that this crew belongs to. 
	 */
	@ManyToOne()
	@JoinColumn(name="TeamId")
	@XmlTransient
	public Team getTeam() {
		return this.team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	
	/**
	 * rowers: Property that links this Crew with all the rowers participating in it.
	 */
	@OneToMany(mappedBy="crew", fetch=FetchType.EAGER)
	@XmlElement(name="rowers")
	public List<CrewRower> getRowers() {
		return this.rowers;
	}
	
	public void setRowers(List<CrewRower> rowers) {
		this.rowers = rowers;
	}
	
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this Event item.
	 * 
	 *  This property is used to update devices only if changed.
	 */
	@Column(name="Timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlTransient
	public Date getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
	/**
	 * status: Property to manage Lifecycle status of this object. 
	 */
	@Column(name="LCStatus")
	@Enumerated(value=EnumType.STRING)
	@XmlElement(name="status")
	public CrewLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(CrewLifeCycleStatus status) {
		this.status = status;
	}
	
}
