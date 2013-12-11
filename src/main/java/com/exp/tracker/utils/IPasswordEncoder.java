package com.exp.tracker.utils;

public interface IPasswordEncoder {
	public String getHash(String id, String pass);
}
