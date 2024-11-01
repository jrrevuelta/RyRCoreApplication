package org.jrrevuelta.rr.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.jrrevuelta.rr.model.BearerToken.TokenLifeCycleStatus;
import org.jrrevuelta.rr.model.BearerToken.TokenType;

@Generated(value="Dali", date="2024-10-31T18:53:52.266+0100")
@StaticMetamodel(BearerToken.class)
public class BearerToken_ {
	public static volatile SingularAttribute<BearerToken, Integer> id;
	public static volatile SingularAttribute<BearerToken, String> accessToken;
	public static volatile SingularAttribute<BearerToken, TokenType> tokenType;
	public static volatile SingularAttribute<BearerToken, Integer> expiration;
	public static volatile SingularAttribute<BearerToken, Date> timestamp;
	public static volatile SingularAttribute<BearerToken, TokenLifeCycleStatus> status;
	public static volatile SingularAttribute<BearerToken, User> user;
}
