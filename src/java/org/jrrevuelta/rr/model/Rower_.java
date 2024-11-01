package org.jrrevuelta.rr.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Rower.RowerLifeCycleStatus;

@Generated(value="Dali", date="2024-10-31T18:53:52.349+0100")
@StaticMetamodel(Rower.class)
public class Rower_ {
	public static volatile SingularAttribute<Rower, Integer> id;
	public static volatile SingularAttribute<Rower, String> name;
	public static volatile SingularAttribute<Rower, String> lastnameP;
	public static volatile SingularAttribute<Rower, String> lastnameM;
	public static volatile SingularAttribute<Rower, Team> team;
	public static volatile SingularAttribute<Rower, Date> timestamp;
	public static volatile SingularAttribute<Rower, RowerLifeCycleStatus> status;
}
