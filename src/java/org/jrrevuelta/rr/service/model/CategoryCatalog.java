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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * CategoryCatalog: Catalog class of the Data Model for the R&R Service.
 * 
 * A CategoryCatalog contains ...
 * 
 * At this moment it will only be used to "tag" and contraint the categories of events, 
 * later, this element will be used to validate rowers that compete in any event.
 * TODO: Include the other pieces of information (in the DB) that are used to validate the rower in the event.
 * 
 * @author José Ramón Revuelta
 */
@Entity
@Table(name="CategoryCatalog")
@Access(AccessType.PROPERTY)
@XmlRootElement(name="category")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CategoryCatalog implements Serializable {

	private int id;
	private String idfr;
	private String displayName;
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.model");
    
	public CategoryCatalog() {
		super();
		log.finest("RR: CategoryCatalog (Catalog model bean - JAXB - JPA) instantiated.");
	}
	
	/**
	 * id: Property with the internal identity of this entity
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	@XmlTransient
	public int getId() {
		return id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * idfr: Property with the external identity of this entity. The standard "key" for the category.
	 * 
	 *  eg: L = Lightweight, U23 = Under 23, MS = Master...
	 */
	@Column(name="IDFR")
	@XmlAttribute
	public String getIdfr() {
		return idfr;
	}
	
	public void setIdfr(String idfr) {
		this.idfr = idfr;
	}
	
	
	/**
	 * displayName: Property that is used to present the name of the category in "short" contexts. 
	 * 
	 * This will be I18N later... for now, it contains the Spanish tag to use.
	 */
	@Column(name="DisplayName")
	@XmlValue
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}