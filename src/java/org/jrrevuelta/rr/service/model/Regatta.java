package org.jrrevuelta.rr.service.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Regatta: Core class of the Data Model for the R&R Service.
 * 
 * A Regatta represents the main event of rowing that groups a series of events, races, etc.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Regatta")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Regatta.all",
			    query="SELECT r FROM Regatta r"),
	@NamedQuery(name="Regatta.sinceLastUpdate",
	            query="SELECT r FROM Regatta r WHERE r.timestamp > :lastUpdate")
})
@XmlRootElement(name="regatta")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Regatta implements Serializable {
	
	private int id;
	private String name;
	private String displayName;
	private byte[] logo;
	private LocalDate startDate;
	private LocalDate endDate;
	private VenueCatalog venue;
	private List<Event> events;
	private Timestamp timestamp;
	private Status status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public Regatta() {
		super();
		log.finest("RR: Regatta (Core model bean - JAXB - JPA) instantiated.");
	}
	
	// Life-cycle status 
	public enum Status {
		INVITATION,
		BOOKING_OPEN,
		BOOKING_CLOSED,
		SCHEDULED,
		IN_PROGRESS,
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
	 * name: Property that fully describes the Regatta. The proper name. 
	 */
	@Column(name="Name")
	@XmlElement(name="name")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * displayName: Property that is used to present the name of the regatta in "short" contexts. 
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
	 * logo: Property that contains the logo of the regatta (or the organizer) to display in the app to be visually recognizable.
	 */
	@Column(name="Logo")
	@Lob
	@XmlTransient
	public byte[] getLogo() {
		return this.logo;
	}
	
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	
	
	/**
	 * startDate: Property for the starting date of this Regatta item.
	 * 
	 *  This property is used as a piece of information, and by the system to consider regatta days 
	 *  for several reasons, such as: display of this regatta up front, timeframes that allow regatta updates, etc.
	 */
	@Column(name="StartDT")
	@XmlElement(name="startDate")
	public LocalDate getStartDate() {
		return this.startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	
	/**
	 * endDate: Property for the ending date of this Regatta item.
	 * 
	 *  This property is used as a piece of information, and by the system to consider regatta days 
	 *  for several reasons, such as: display of this regatta up front, timeframes that allow regatta updates, etc.
	 */
	@Column(name="EndDT")
	@XmlElement(name="endDate")
	public LocalDate getEndDate() {
		return this.endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	
	/**
	 * venue: Property that holds the location or venue of this regatta. 
	 */
	@ManyToOne()
	@JoinColumn(name="VenueId")
	@XmlTransient
	public VenueCatalog getVenue() {
		return this.venue;
	}
	
	public void setVenue(VenueCatalog venue) {
		this.venue = venue;
	}
	
	@Transient
	@XmlElement(name="venueName")
	public String getVenueName() {
		return getVenue() != null ? this.getVenue().getName() : null;
	}
	public void setVenueName(String venueName) { }
	
	@Transient
	@XmlElement(name="venueId", nillable=true) // TODO: Setter needed when creating the regatta element
	public int getVenueId() {
		return getVenue() != null ? this.getVenue().getId() : 0;
	}
	public void setVenueId(int venueId) { }
	
	
	/**
	 * events: Property that links this Regatta with its corresponding Events
	 */
	@OneToMany(mappedBy="regatta")
	@XmlTransient
	public List<Event> getEvents() {
		return this.events;
	}
	
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this Regatta item.
	 * 
	 *  This property is used to update devices only if changed.
	 */
	@Column(name="Timestamp")
	@XmlTransient
	public Timestamp getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	
	/**
	 * status: Property to manage Lifecycle status of this object. 
	 */
	@Column(name="LCStatus")
	@Enumerated(value=EnumType.STRING)
	@XmlElement(name="status")
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
}
