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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * BearerToken: Representation of an access token used as a 'bearer token' as described in [RFC6750].
 *
 * A token is issued to any verified user, to make requests to the service in any authorized access 
 * point, the call must include the valid bearer token. The expiration time of the token may vary 
 * depending on the scope given to the users.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="BearerToken")
@Access(AccessType.PROPERTY)
@NamedQueries({
	@NamedQuery(name="BearerToken.all",
			    query="SELECT t FROM BearerToken t"),
	@NamedQuery(name="Token.withAccessToken",
	            query="SELECT t FROM BearerToken t WHERE t.accessToken = :accessToken")
})
@XmlRootElement(name="token")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BearerToken implements Serializable {
	
	private int id;
	private String accessToken;
	private TokenType tokenType;
	private int expiration;
	private Date timestamp;
	private TokenLifeCycleStatus status;
	private User user;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
	
	public enum TokenType {
		Bearer,
		MAC
	}
	
	public enum TokenLifeCycleStatus {
		ACTIVE,
		EXPIRED,
		CANCELED
	}
	
	
	public BearerToken() {
		super();
		log.finest("RR: BearerToken (Model bean - JAXB - JPA) instantiated.");
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
	 * accessToken: Property that uniquely identifies a user in a user-oriented way 
	 */
	@Column(name="AccessToken")
	@XmlElement(name="access-token")
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
	/**
	 * tokenType: Property that determines the type of token.
	 */
	@Column(name="TokenType")
	@Enumerated(value=EnumType.STRING)
	@XmlElement(name="token-type")
	public TokenType getTokenType() {
		return this.tokenType;
	}
	
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	
	
	/**
	 * expiration: Property that specifies the expiration time in seconds for this token (while ACTIVE) 
	 */
	@Column(name="Expiration")
	@XmlElement(name="expires-in")
	public int getExpiration() {
		return this.expiration;
	}
	
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}
	
	
	/**
	 * timestamp: Property that holds moment in time of the last update to this Token item.
	 * 
	 * When delivered to the client as ACTIVE, it holds the 'issued-at' time, so the client can
	 * calculate the life span of the token.
	 */
	@Column(name="Timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlElement(name="issued-at")
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
	public TokenLifeCycleStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(TokenLifeCycleStatus status) {
		this.status = status;
	}
	

	/**
	 * user: Property that holds the user that this token belongs to. 
	 */
	@ManyToOne()
	@JoinColumn(name="UserId")
	@XmlTransient
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	
	@Transient
	@XmlTransient
	public boolean isValid() {
		if (getStatus() == TokenLifeCycleStatus.ACTIVE) {
			Date now = new Date();
			Date expirationTime = new Date(getTimestamp().getTime() + (getExpiration() * 1000));
			if (now.before(expirationTime)) {
				return true;
			} else {
				setStatus(TokenLifeCycleStatus.EXPIRED);
			}
		}
		return false;
	}
	
}
