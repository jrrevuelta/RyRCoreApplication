package org.jrrevuelta.rr.model;

import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Role: The different roles that each user in the system may represent.
 * 
 * Roles describe the capabilities and authorities that a user may have in the system.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="Role")
@Access(AccessType.PROPERTY)
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.model");
	
	private int id;
	private String idfr;
	private Roles role;
	private User user;

	public enum Roles {
		ROWER,
		COACH,
		DELEGATE,
		JUDGE,
		ADMIN
	}

	public Role() {
		super();
		log.finest("RR: Role (Model bean - JAXB - JPA) instantiated.");
	}

	
	/**
	 * id: Internal identity of this entity.
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	@JsonbTransient
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }

	/**
	 * idfr: Property that uniquely identifies a user in a user-oriented way. 
	 */
	@Column(name="IDFR")
	@JsonbTransient
	public String getIdfr() { return this.idfr; }
	public void setIdfr(String idfr) { this.idfr = idfr; }

	/**
	 * role: Property that assigns a single role to the corresponding user.
	 * 
	 * Each role of a user should be uniquely assigned.
	 */
	@Column(name="Role")
	@Enumerated(EnumType.STRING)
	public Roles getRole() { return this.role; }
	public void setRole(Roles role) { this.role = role; }

	/**
	 * user: Property that references the corresponding user.
	 */
	@ManyToOne @JoinColumn(name="UserId")
	@JsonbTransient
	public User getUser() { return this.user; }
	public void setUser(User user) { this.user = user; }

}