package org.jrrevuelta.rr.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Event.EventLifeCycleStatus;

@Generated(value="Dali", date="2024-10-31T18:53:52.343+0100")
@StaticMetamodel(Event.class)
public class Event_ {
	public static volatile SingularAttribute<Event, Integer> id;
	public static volatile SingularAttribute<Event, CategoryCatalog> category;
	public static volatile SingularAttribute<Event, GenderCatalog> gender;
	public static volatile SingularAttribute<Event, BoatCatalog> boat;
	public static volatile SingularAttribute<Event, Integer> distance;
	public static volatile SingularAttribute<Event, Regatta> regatta;
	public static volatile ListAttribute<Event, Race> races;
	public static volatile SingularAttribute<Event, Date> timestamp;
	public static volatile SingularAttribute<Event, EventLifeCycleStatus> status;
}
