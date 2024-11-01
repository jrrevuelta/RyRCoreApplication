package org.jrrevuelta.rr.model;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Invitation.Status;
import org.jrrevuelta.rr.model.Role.Roles;

@Generated(value="Dali", date="2024-11-01T16:38:28.780+0100")
@StaticMetamodel(Invitation.class)
public class Invitation_ {
	public static volatile SingularAttribute<Invitation, Integer> id;
	public static volatile SingularAttribute<Invitation, String> idfr;
	public static volatile SingularAttribute<Invitation, String> name;
	public static volatile SingularAttribute<Invitation, String> email;
	public static volatile SingularAttribute<Invitation, User> authority;
	public static volatile SingularAttribute<Invitation, Roles> role;
	public static volatile SingularAttribute<Invitation, Status> status;
	public static volatile SingularAttribute<Invitation, String> lastname;
	public static volatile SingularAttribute<Invitation, Timestamp> timestamp;
}
