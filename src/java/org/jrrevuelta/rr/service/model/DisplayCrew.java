package org.jrrevuelta.rr.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="crew")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DisplayCrew {
	
	private int crewId;
	private int lane;
	private int teamId;
	private String crewName;
	private boolean combined;
	private List<DisplayRower> rowers;
	
	public DisplayCrew() {
		super();
	}
	
	public DisplayCrew(int crewId, int lane, String crewName, boolean combined) {
		super();
		this.crewId = crewId;
		this.lane = lane;
		this.crewName = crewName;
		this.combined = combined;
	}
	
	
	@XmlAttribute
	public int getCrewId() {
		return crewId;
	}
	public void setCrewId(int crewId) {
		this.crewId = crewId;
	}
	
	@XmlElement
	public int getLane() {
		return lane;
	}
	public void setLane(int lane) {
		this.lane = lane;
	}
	
	@XmlElement
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	@XmlElement
	public String getCrewName() {
		return crewName;
	}
	public void setCrewName(String crewName) {
		this.crewName = crewName;
	}

	@XmlElement
	public boolean isCombined() {
		return combined;
	}
	public void setCombined(boolean combined) {
		this.combined = combined;
	}

	@XmlElement
	public List<DisplayRower> getRowers() {
		return rowers;
	}
	public void setRowers(List<DisplayRower> rowers) {
		this.rowers = rowers;
	}
	
}
