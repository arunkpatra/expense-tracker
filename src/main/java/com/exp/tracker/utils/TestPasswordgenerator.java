package com.exp.tracker.utils;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class TestPasswordgenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		printHash("Tom", "password");
	}
	
	private static void printHash(String id, String pass) {
		System.out.println("[User/Password = " + id + "/" + pass + "], [Hash=" + getHash(id, pass) + "]");
	}
	private static String getHash(String id, String pass) {
		MessageDigestPasswordEncoder mdpe = new MessageDigestPasswordEncoder("MD5");
		return mdpe.encodePassword(pass, id);
	}

}
