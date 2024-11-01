package org.jrrevuelta.rr.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.Role.Roles;

@Generated(value="Dali", date="2024-11-01T00:44:45.908+0100")
@StaticMetamodel(Role.class)
public class Role_ {
	public static volatile SingularAttribute<Role, Integer> id;
	public static volatile SingularAttribute<Role, String> idfr;
	public static volatile SingularAttribute<Role, Roles> role;
	public static volatile SingularAttribute<Role, User> user;
}
