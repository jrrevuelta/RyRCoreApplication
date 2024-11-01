package org.jrrevuelta.rr.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2024-10-31T18:53:52.347+0100")
@StaticMetamodel(RaceCrew.class)
public class RaceCrew_ {
	public static volatile SingularAttribute<RaceCrew, Race> race;
	public static volatile SingularAttribute<RaceCrew, Crew> crew;
	public static volatile SingularAttribute<RaceCrew, Integer> lane;
}
