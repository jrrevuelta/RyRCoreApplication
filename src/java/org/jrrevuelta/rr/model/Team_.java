package org.jrrevuelta.rr.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Team.TeamLifeCycleStatus;

@Generated(value="Dali", date="2024-10-31T18:53:52.350+0100")
@StaticMetamodel(Team.class)
public class Team_ {
	public static volatile SingularAttribute<Team, Integer> id;
	public static volatile SingularAttribute<Team, String> name;
	public static volatile SingularAttribute<Team, String> displayName;
	public static volatile SingularAttribute<Team, byte[]> logo;
	public static volatile SingularAttribute<Team, byte[]> blade;
	public static volatile ListAttribute<Team, Rower> rowers;
	public static volatile SingularAttribute<Team, Date> timestamp;
	public static volatile SingularAttribute<Team, TeamLifeCycleStatus> status;
}
