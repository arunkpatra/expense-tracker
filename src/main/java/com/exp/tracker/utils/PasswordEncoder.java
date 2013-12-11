package com.exp.tracker.utils;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class PasswordEncoder implements IPasswordEncoder {

	public String getHash(String id, String pass) {
		MessageDigestPasswordEncoder mdpe = new MessageDigestPasswordEncoder("MD5");
		return mdpe.encodePassword(pass, id);
	}

}
