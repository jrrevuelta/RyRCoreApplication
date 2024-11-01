package org.jrrevuelta.rr.model;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.User.Status;

@Generated(value="Dali", date="2024-11-01T01:38:04.036+0100")
@StaticMetamodel(User.class)
public class User_ {
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> idfr;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> phoneNumber;
	public static volatile SingularAttribute<User, Timestamp> timestamp;
	public static volatile SingularAttribute<User, Status> status;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, byte[]> avatar;
	public static volatile ListAttribute<User, Role> roles;
}
