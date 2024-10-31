package org.jrrevuelta.rr.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rower")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DisplayRower {
	
	private String name;
	private String lastname;
	private String seat;
	
	
	public DisplayRower() {
		super();
	}
	
	public DisplayRower(String name, String lastname, String seat) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.seat = seat;
	}
	
	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	@XmlElement
	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}
	
	
}
