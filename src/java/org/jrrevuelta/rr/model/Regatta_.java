package org.jrrevuelta.rr.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Regatta.Status;

@Generated(value="Dali", date="2024-10-31T18:53:52.348+0100")
@StaticMetamodel(Regatta.class)
public class Regatta_ {
	public static volatile SingularAttribute<Regatta, Integer> id;
	public static volatile SingularAttribute<Regatta, String> name;
	public static volatile SingularAttribute<Regatta, String> displayName;
	public static volatile SingularAttribute<Regatta, byte[]> logo;
	public static volatile SingularAttribute<Regatta, LocalDate> startDate;
	public static volatile SingularAttribute<Regatta, LocalDate> endDate;
	public static volatile SingularAttribute<Regatta, VenueCatalog> venue;
	public static volatile ListAttribute<Regatta, Event> events;
	public static volatile SingularAttribute<Regatta, Timestamp> timestamp;
	public static volatile SingularAttribute<Regatta, Status> status;
}
