package org.jrrevuelta.rr.service.model;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * LocationCatalog: Catalog class of the Data Model for the R&R Service.
 * 
 * A LocationCatalog contains the venues that may be used for a rowing competition.
 * It is important to maintain this catalog, so that the different venues are identified
 * in each case so that they may be used by users to filter out "local" events.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="VenueCatalog")
@Access(AccessType.PROPERTY)
@XmlRootElement(name="location")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class VenueCatalog implements Serializable {

	private int id;
	private String name;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	
	public VenueCatalog() {
		super();
		log.finest("RR: VenueCatalog (Catalog model bean - JAXB - JPA) instantiated.");
	}
	
	/**
	 * id: Property with the internal identity of this entity
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	@XmlAttribute
	public int getId() {
		return id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * name: Property that fully describes the Venue. The proper name of the venue. 
	 */
	@Column(name="Name")
	@XmlElement(name="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
