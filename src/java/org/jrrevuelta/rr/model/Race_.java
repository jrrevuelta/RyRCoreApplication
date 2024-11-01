package org.jrrevuelta.rr.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Race.RaceLifeCycleStatus;

@Generated(value="Dali", date="2024-10-31T18:53:52.346+0100")
@StaticMetamodel(Race.class)
public class Race_ {
	public static volatile SingularAttribute<Race, Integer> id;
	public static volatile SingularAttribute<Race, Event> event;
	public static volatile SingularAttribute<Race, Integer> raceNumber;
	public static volatile SingularAttribute<Race, Date> startTime;
	public static volatile SingularAttribute<Race, String> progression;
	public static volatile ListAttribute<Race, RaceCrew> crews;
	public static volatile SingularAttribute<Race, Date> timestamp;
	public static volatile SingularAttribute<Race, RaceLifeCycleStatus> status;
}
