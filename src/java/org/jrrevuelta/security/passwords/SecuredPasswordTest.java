package org.jrrevuelta.security.passwords;
import java.math.BigInteger;

public class SecuredPasswordTest {

	public static void main(String[] args) throws Exception {
		
		String pass1 = "Good";
		String pass2 = "Wrong";
		String pass3 = "good";
		
		// Generate the secure version of the password to keep in the DB
		SecuredPasswordGenerator gen = new SecuredPasswordGenerator();
		SecuredPassword p = gen.generateNewSecuredPassword(pass1);
		
		// This is what is kept in the DB
		System.out.println("DK:   " + new BigInteger(1, p.getDerivedKey()).toString(16).toUpperCase());
		System.out.println("Salt: " + new BigInteger(1, p.getSalt()).toString(16).toUpperCase());
		System.out.println("Counter: " + p.getCounter() + "\n");
		
		// Generate a verifier based on the stored secured password
		SecuredPasswordVerifier ver = new SecuredPasswordVerifier(p);
		
		// Verify claimed passwords against the stored one
		System.out.println(pass1 + ": " + ver.verifyPassword(pass1));  // This is the ONLY correct password
		System.out.println(pass2 + ": " + ver.verifyPassword(pass2));
		System.out.println(pass3 + ": " + ver.verifyPassword(pass3));
	}

}
