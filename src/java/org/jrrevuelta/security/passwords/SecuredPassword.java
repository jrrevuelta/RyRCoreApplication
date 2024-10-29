package org.jrrevuelta.security.passwords;

import java.util.logging.Logger;

public class SecuredPassword {
	
	private byte[] derivedKey;
	private byte[] salt;
	private int counter;

	private static Logger log = Logger.getLogger("org.jrrevuelta.security.passwords");

	
	public SecuredPassword() {
		super();
		log.finest("JRR-Security: SecuredPassword model object instantiated.");
	}
	
	public SecuredPassword(byte[] derivedKey, byte[] salt, int counter) {
		this();
		setDerivedKey(derivedKey);
		setSalt(salt);
		setCounter(counter);
	}
	
	
	public byte[] getDerivedKey() {
		return derivedKey;
	}
	public void setDerivedKey(byte[] derivedKey) {
		this.derivedKey = derivedKey;
	}
	
	public byte[] getSalt() {
		return salt;
	}
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	
}
