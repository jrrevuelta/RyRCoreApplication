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
 * Race: Core class of the Data Model for the R&R Service.
 * 
 * A Race represents a single race stage of an event in the competition. 
 * All the races in the progression are related to the single event for them.
 * 
 * ** The results are related to their specific races.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Race")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Race.all.forEvent",
            query="SELECT r FROM Race r WHERE r.event = :event"),
	@NamedQuery(name="Race.forEvent.sinceLastUpdate",
            query="SELECT r FROM Race r WHERE r.event = :event AND r.timestamp > :lastUpdate"),
	@NamedQuery(name="Race.all.forRegatta",
            query="SELECT r FROM Race r WHERE r.event.regatta = :regatta"),
	@NamedQuery(name="Race.forRegatta.sinceLastUpdate",
            query="SELECT r FROM Race r WHERE r.event.regatta = :regatta AND r.timestamp > :lastUpdate")
})
@XmlRootElement(name="race")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Race implements Serializable {
	
	private int id;
	private Event event;
	private int raceNumber;
	private Date startTime;
	private String progression;
	private List<RaceCrew> crews;
	private Date timestamp;
	private RaceLifeCycleStatus status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public Race() {
		super();
		log.finest("RR: Race (Core model bean - JAXB - JPA) instantiated.");
	}
	
	// Life-cycle status 
	public enum RaceLifeCycleStatus {
		INVITATION,
		BOOKING_OPEN,
		BOOKING_CLOSED,
		PENDING_PROGRESSION,
		PROGRAMMED,
		IN_PROGRESS,
		DESERTED,
		FINALIZED
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
	 * event: Property that holds the event that this race belongs to. 
	 */
	@ManyToOne()
	@JoinColumn(name="EventId")
	@XmlElement(name="event")
	public Event getEvent() {
		return this.event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
	/**
	 * raceNumber: Property that refers to the race number in the regatta's program. 
	 */
	@Column(name="RaceNumber")
	@XmlElement(name="raceNumber")
	public int getRaceNumber() {
		return this.raceNumber;
	}
	
	public void setRaceNumber(int raceNumber) {
		this.raceNumber = raceNumber;
	}
	
	
	/**
	 * startTime: Property that holds date/time in which the race takes place.
	 */
	@Column(name="startTime")
	@Temporal(TemporalType.DATE)
	@XmlElement(name="startTime")
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	/**
	 * progression: Property that describes the phase of this race on the progression of this particular event.
	 * 
	 *  It is coded by the FISA nomenclature such as: F = Final, SF = Semi-Final, QF = Quarter-Final, R = Repechage...
	 *  FA, FB = Final A, B... H1, H2, H3... = Heat 1, 2, 3...
	 */
	@Column(name="Progression")
	@XmlElement(name="progression")
	public String getProgression() {
		return this.progression;
	}
	
	public void setProgression(String progression) {
		this.progression = progression;
	}
	
	
	/**
	 * crews: ...
	 */
	@OneToMany(mappedBy="race", fetch=FetchType.EAGER)
	@XmlTransient
	public List<RaceCrew> getCrews() {
		return this.crews;
	}
	
	public void setCrews(List<RaceCrew> crews) {
		this.crews = crews;
	}
	
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this Race item.
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
	public RaceLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(RaceLifeCycleStatus status) {
		this.status = status;
	}
	
}
