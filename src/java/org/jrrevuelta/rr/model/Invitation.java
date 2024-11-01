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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
 * Invitation: Object that provisions a new User in the system. 
 * 
 * When a user authorized to invite (COACH, DELEGATE, JUDGE and ADMIN) invites a new subject, 
 * an invitation is provisioned. And when the invitee invokes the Sign-In method, this object
 * is recalled to prepare the enrollment process that is about to start.
 * 
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Invitation")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="Invitation.all", query="SELECT i FROM Invitation i"),
	@NamedQuery(name="Invitation.withIDFR", query="SELECT i FROM Invitation i WHERE i.idfr = :idfr")
})
@XmlRootElement(name="invitation")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Invitation implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
	
	private int id;
	private String idfr;
	private String name;
	private String lastName;
	private String email;

	private Role.Roles role;
	private User authority;   // TODO: link with inviting User (the authority) when enrollment and token issuing are complete (obtained from the Bearer Token)
	private Date timestamp;
	private Status status;
	
	public enum Status {
		CREATED,
		EXPIRED,
		DELETED,
		CANCELED,
	}
	
	public Invitation() {
		super();
		log.finest("RR: Invitation entity instantiated.");
	}
	
	
	/**
	 * id: PK - Internal identity of this entity
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	@XmlAttribute
	public int getId() { return this.id; }
	protected void setId(int id) { this.id = id; }
	
	/**
	 * idfr: Property that uniquely identifies the invitation and is exposed to the user.
	 *  
	 * This is the element that is included in the invitation e-mail. 
	 */
	@Column(name="IDFR")
	@XmlElement(name="idfr")
	public String getIdfr() { return this.idfr; }
	public void setIdfr(String idfr) { this.idfr = idfr; }
	
	/**
	 * name: Property that contains the proper name of the individual. 
	 */
	@Column(name="Name")
	@XmlElement(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 * lastName: Property that contains the family name or last-name of the individual. 
	 */
	@Column(name="lastName")
	@XmlElement(name="lastName")
	public String getLastName() { return this.lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	
	/**
	 * email: Property that contains the email of the individual. 
	 */
	@Column(name="email")
	@XmlElement(name="email")
	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }
	
	/**
	 * role: Property that holds the designated role for the person to invite.
	 */
	@Column(name="Role")
	@Enumerated(value=EnumType.STRING)
	public Role.Roles getRole() {
		return this.role;
	}
	public void setRole(Role.Roles role) {
		this.role = role;
	}
	
	
	/**
	 * authority: Property that holds the user that issued this invitation (who has the authority to do it). 
	 */
	// @ManyToOne()
	// @JoinColumn(name="AuthorityUserId")
	@Transient
	@XmlTransient
	public User getAuthority() {
		return this.authority;
	}
	
	public void setAuthority(User authority) {
		this.authority = authority;
	}
	
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this User item.
	 * 
	 *  This property is used for accountability purposes.
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
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
}
