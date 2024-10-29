package org.jrrevuelta.rr.service.model;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Event: Core class of the Data Model for the R&R Service.
 * 
 * An Event represents a single competition for a specific category, gender and boat type. 
 * It defines the distance of the event, and all the races in the progression are related to the
 * single event for them. Also all the participant crews are related to the single event.
 * 
 * ** The results are related to their specific races but there is a final result for the event.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Event")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Event.all.forRegatta",
            query="SELECT e FROM Event e WHERE e.regatta = :regatta ORDER BY e.id"),
	@NamedQuery(name="Event.forRegatta.sinceLastUpdate",
            query="SELECT e FROM Event e WHERE e.regatta = :regatta AND e.timestamp > :lastUpdate")
})
@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Event implements Serializable {
	
	private int id;
	private CategoryCatalog category;
	private GenderCatalog gender;
	private BoatCatalog boat;
	private int distance;
	private Regatta regatta;
	private List<Race> races;
	private Date timestamp;
	private EventLifeCycleStatus status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public Event() {
		super();
		log.finest("RR: Event (Core model bean - JAXB - JPA) instantiated.");
	}
	
	// Life-cycle status 
	public enum EventLifeCycleStatus {
		INVITATION,
		BOOKING_OPEN,
		BOOKING_CLOSED,
		SCHEDULED,
		IN_PROGRESS,
		DESERTED,
		FINISHED
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
	 * idfr: FISA code of the event (read only)
	 */
	@Transient
	@XmlElement(name="idfr")
	public String getIdfr() {
		return getCategory().getIdfr() + getGender().getIdfr() + getBoat().getIdfr();
	}
	
	
	/**
	 * category: Property that holds the category of this event. 
	 */
	@ManyToOne()
	@JoinColumn(name="CategoryId")
	@XmlElement(name="category")
	public CategoryCatalog getCategory() {
		return this.category;
	}
	
	public void setCategory(CategoryCatalog category) {
		this.category = category;
	}
	

	/**
	 * gender: Property that holds the gender of this event. 
	 */
	@ManyToOne()
	@JoinColumn(name="GenderId")
	@XmlElement(name="gender")
	public GenderCatalog getGender() {
		return this.gender;
	}
	
	public void setGender(GenderCatalog gender) {
		this.gender = gender;
	}
	

	/**
	 * boat: Property that holds the boat type of this event. 
	 */
	@ManyToOne()
	@JoinColumn(name="BoatId")
	@XmlElement(name="boat")
	public BoatCatalog getBoat() {
		return this.boat;
	}
	
	public void setBoat(BoatCatalog boat) {
		this.boat = boat;
	}
	
	
	/**
	 * distance: Property that holds the distance of this event. 
	 */
	@Column(name="Distance")
	@XmlElement(name="distance")
	public int getDistance() {
		return this.distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	
	/**
	 * regatta: Property that holds the regatta that this event belongs to. 
	 */
	@ManyToOne()
	@JoinColumn(name="RegattaId")
	@XmlTransient
	public Regatta getRegatta() {
		return this.regatta;
	}
	
	public void setRegatta(Regatta regatta) {
		this.regatta = regatta;
	}
	
	
	/**
	 * races: Property that links this Events with all the races in the progression
	 */
	@OneToMany(mappedBy="event")
	@XmlTransient
	public List<Race> getRaces() {
		return this.races;
	}
	
	public void setRaces(List<Race> races) {
		this.races = races;
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
	public EventLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(EventLifeCycleStatus status) {
		this.status = status;
	}
	
}
