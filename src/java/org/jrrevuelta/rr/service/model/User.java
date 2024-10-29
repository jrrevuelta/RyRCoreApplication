package org.jrrevuelta.rr.service.model;

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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * User: Representation of any identified user of the system.
 * 
 * Users may have many roles, which define the capabilities and authorizations that users have
 * in order to make things in the system.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="User")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="User.all",
			    query="SELECT u FROM User u"),
	@NamedQuery(name="User.withIDFR",
	            query="SELECT u FROM User u WHERE u.idfr = :idfr")
})
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class User implements Serializable {
	
	private int id;
	private String idfr;
	private String name;
	private String lastName;
	private byte[] password;
	private byte[] salt;
	private int counter;
	private String email;
	private String phoneNumber;
	private UserRole role;
	private Date timestamp;
	private UserLifeCycleStatus status;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	// Life-cycle status 
	public enum UserLifeCycleStatus {
		ACTIVE,
		SUSPENDED,
		DELETED,
		CANCELED,
	}
	
	public User() {
		super();
		log.finest("RR: User (Model bean - JAXB - JPA) instantiated.");
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
	 * idfr: Property that uniquely identifies a user in a user-oriented way 
	 */
	@Column(name="IDFR")
	@XmlElement(name="idfr")
	public String getIdfr() {
		return this.idfr;
	}
	
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	
	/**
	 * name: Property that contains the proper name of the individual. 
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
	 * lastName: Property that contains the family name or last-name of the individual. 
	 */
	@Column(name="lastName")
	@XmlElement(name="lastName")
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	/**
	 * password: Property that holds the encrypted representation of the user's password.
	 */
	@Column(name="Password")
	@XmlTransient
	public byte[] getPassword() {
		return this.password;
	}
	
	public void setPassword(byte[] password) {
		this.password = password;
	}
	
	
	/**
	 * salt: Property that hold the salt component for the password representation.
	 */
	@Column(name="Salt")
	@XmlTransient
	public byte[] getSalt() {
		return this.salt;
	}
	
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
	
	/**
	 * counter: Property that holds the counter parameter for the password representation.
	 */
	@Column(name="Counter")
	@XmlTransient
	public int getCounter() {
		return this.counter;
	}
	
	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	
	/**
	 * email: Property that contains the email of the individual. 
	 */
	@Column(name="email")
	@XmlElement(name="email")
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	/**
	 * phoneNumber: Property that contains phone number of the individual. 
	 */
	@Column(name="phoneNumber")
	@XmlElement(name="phoneNumber")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	/**
	 * role: Property that holds the designated role for the User.
	 */
	@Column(name="Role")
	@Enumerated(value=EnumType.STRING)
	@XmlElement(name="role")
	public UserRole getRole() {
		return this.role;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
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
	public UserLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(UserLifeCycleStatus status) {
		this.status = status;
	}
	

}
