package org.jrrevuelta.rr.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

import javax.json.bind.annotation.JsonbTransient;
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
import javax.persistence.Transient;

/**
 * User: Representation of any identified user of the system.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="User")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="User.all", query="SELECT u FROM User u"),
	@NamedQuery(name="User.withIDFR", query="SELECT u FROM User u WHERE u.idfr = :idfr")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.model");
	
	private int id;
	private String idfr;
	private String password;
	private String name;
	private String lastName;
	private String email;
	private String phoneNumber;
	private byte[] avatar;
	private Status status;
	private Timestamp timestamp;
	private List<Role> roles;
	
	public enum Status {
		ACTIVE,
		SUSPENDED,
		CANCELED,
		DELETED,
	}
	
	public User() {
		super();
		log.finest("RR: User (Model bean - JAXB - JPA) instantiated.");
	}
	
	
	/**
	 * id: Internal identity of this entity
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	@JsonbTransient
	public int getId() { return this.id; }
	protected void setId(int id) { this.id = id; }
	
	/**
	 * idfr: Property that uniquely identifies a user in a user-oriented way 
	 */
	@Column(name="IDFR")
	public String getIdfr() { return this.idfr; }
	public void setIdfr(String idfr) { this.idfr = idfr; }
	
	/**
	 * password: Property that holds the encrypted representation of the user's password.
	 */
	@Column(name="Password")
	@JsonbTransient
	public String getPassword() { return this.password; }
	public void setPassword(String password) { this.password = password; }
	
	/**
	 * name: Property that contains the proper name of the individual. 
	 */
	@Column(name="Name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 * lastName: Property that contains the family name or last-name of the individual. 
	 */
	@Column(name="Lastname")
	public String getLastName() { return this.lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	
	/**
	 * email: Property that contains the email of the individual. 
	 */
	@Column(name="Email")
	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }
	
	/**
	 * phoneNumber: Property that contains phone number of the individual. 
	 */
	@Column(name="PhoneNumber")
	public String getPhoneNumber() { return this.phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	
	/**
	 * avatar: Property that holds and image to graphically represent and show an individual.
	 */
	@Column(name="Avatar")
	@Lob
	@JsonbTransient
	public byte[] getAvatar() { return avatar; }
	public void setAvatar(byte[] avatar) { this.avatar = avatar; }
	
	@Transient
	public boolean getHasAvatar() { return getAvatar() != null; }
	
	/**
	 * status: Property to manage Lifecycle status of this object. 
	 */
	@Column(name="Status")
	@Enumerated(value=EnumType.STRING)
	public Status getStatus() { return this.status; }
	public void setStatus(Status status) { this.status = status; }
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this User item.
	 * 
	 *  This property is used for accountability purposes.
	 */
	@Column(name="Timestamp")
	@JsonbTransient
	public Timestamp getTimestamp() { return this.timestamp; }
	public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
	
	/**
	 * roles: List with all the designated roles for the User.
	 */
	@OneToMany(mappedBy="user")
	public List<Role> getRoles() { return roles; }
	public void setRoles(List<Role> roles) { this.roles = roles; }
	
}
