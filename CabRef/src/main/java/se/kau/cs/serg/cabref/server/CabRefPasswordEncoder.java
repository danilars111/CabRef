package se.kau.cs.serg.cabref.server;

import org.mindrot.jbcrypt.BCrypt;
import org.pac4j.core.credentials.password.PasswordEncoder;

public class CabRefPasswordEncoder implements PasswordEncoder {
	String salt;
	
	public CabRefPasswordEncoder(String salt) {
		this.salt = salt;
	}

	@Override
	public String encode(String password) {
		System.out.println("ENCODED PASSWORD: " + BCrypt.hashpw(password, salt));
		return BCrypt.hashpw(password, salt);
	}

	@Override
	public boolean matches(String plainPassword, String encodedPassword) {
		System.out.println(BCrypt.hashpw(plainPassword, salt));
		System.out.println(encodedPassword);
		
		if(BCrypt.hashpw(plainPassword, salt).equals(encodedPassword)) {
			return true;
		} else {
			return false;
		}
	}

}