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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * Team: Core class of the Data Model for the R&R Service.
 * 
 * A Team represents each of the squads ascribed to the rowing federation.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Team")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Team.all",
			    query="SELECT t FROM Team t"),
	@NamedQuery(name="Team.sinceLastUpdate",
	            query="SELECT t FROM Team t WHERE t.timestamp > :lastUpdate")
})
@XmlRootElement(name="team")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Team implements Serializable {
	
	private int id;
	private String name;
	private String displayName;
	private byte[] logo;
	private byte[] blade;
	private List<Rower> rowers;
	private Date timestamp;
	private TeamLifeCycleStatus status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public Team() {
		super();
		log.finest("RR: Team (Core model bean - JAXB - JPA) instantiated.");
	}
	
	// Life-cycle status 
	public enum TeamLifeCycleStatus {
		REGISTERED,
		INVITED,
		REMOVED
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
	 * name: Property that fully describes the Team. The proper name. 
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
	 * displayName: Property that is used to present the name of the team in "short" contexts. 
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
	 * logo: Property that contains the logo of the team to display in the app to be visually recognizable.
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
	 * blade: Property that contains the colors of the blades of the team to display in the app to be visually recognizable.
	 */
	@Column(name="Blade")
	@Lob
	@XmlTransient
	public byte[] getBlade() {
		return this.blade;
	}
	
	public void setBlade(byte[] blade) {
		this.blade = blade;
	}
	
	
	/**
	 * rowers: Property that links this Team with its member Rowers
	 */
	@OneToMany(mappedBy="team")
	@XmlTransient
	public List<Rower> getRowers() {
		return this.rowers;
	}
	
	public void setRowers(List<Rower> rowers) {
		this.rowers = rowers;
	}
	
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this Team item.
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
	public TeamLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(TeamLifeCycleStatus status) {
		this.status = status;
	}
	
}
