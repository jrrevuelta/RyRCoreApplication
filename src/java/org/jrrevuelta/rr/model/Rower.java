package org.jrrevuelta.rr.model;

import java.io.Serializable;
import java.util.Date;
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
 * Rower: Core class of the Data Model for the R&R Service.
 * 
 * A Rower represents any person that participates in the rowing regattas by competing in a crew.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Rower")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Rower.all.forTeam",
            query="SELECT r FROM Rower r WHERE r.team = :team"),
	@NamedQuery(name="Rower.forTeam.sinceLastUpdate",
            query="SELECT r FROM Rower r WHERE r.team = :team AND r.timestamp > :lastUpdate")
})
@XmlRootElement(name="rower")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Rower implements Serializable {
	
	private int id;
	private String name;
	private String lastnameP;
	private String lastnameM;
	private Team team;
	private Date timestamp;
	private RowerLifeCycleStatus status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public Rower() {
		super();
		log.finest("RR: Event (Core model bean - JAXB - JPA) instantiated.");
	}
	
	// Life-cycle status 
	public enum RowerLifeCycleStatus {
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
	 * name: Property that holds the given name of the rower. 
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
	 * lastnameP: Property that holds the family name of the rower (apellido paterno). 
	 */
	@Column(name="LastnamePat")
	@XmlElement(name="lastnameP")
	public String getLastnameP() {
		return this.lastnameP;
	}
	
	public void setLastnameP(String lastnameP) {
		this.lastnameP = lastnameP;
	}
	
	
	/**
	 * lastnameM: Property that holds the family name of the rower (apellido materno). 
	 */
	@Column(name="LastnameMat")
	@XmlElement(name="lastnameM")
	public String getLastnameM() {
		return this.lastnameM;
	}
	
	public void setLastnameM(String lastnameM) {
		this.lastnameM = lastnameM;
	}
	
	
	/**
	 * team: Property that holds the team that this rower belongs to. 
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
	public RowerLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(RowerLifeCycleStatus status) {
		this.status = status;
	}
	
}
