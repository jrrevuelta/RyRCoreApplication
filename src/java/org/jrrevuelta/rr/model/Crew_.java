package org.jrrevuelta.rr.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Crew.CrewLifeCycleStatus;

@Generated(value="Dali", date="2024-10-31T18:53:52.340+0100")
@StaticMetamodel(Crew.class)
public class Crew_ {
	public static volatile SingularAttribute<Crew, Integer> id;
	public static volatile SingularAttribute<Crew, String> displayName;
	public static volatile SingularAttribute<Crew, Boolean> combined;
	public static volatile SingularAttribute<Crew, Event> event;
	public static volatile SingularAttribute<Crew, Team> team;
	public static volatile ListAttribute<Crew, CrewRower> rowers;
	public static volatile SingularAttribute<Crew, Date> timestamp;
	public static volatile SingularAttribute<Crew, CrewLifeCycleStatus> status;
}
