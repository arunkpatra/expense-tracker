package com.exp.tracker.data.model;

import java.io.Serializable;

import com.exp.tracker.data.entities.AuthEntity;

public class AuthBean implements Serializable{
	private static final long serialVersionUID = -5955775199046995695L;
	
	private Long id;
	private String username;	
	private String authority;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public AuthBean(AuthEntity ae) {
		this.id = ae.getId();
		this.username = ae.getUsername();
		this.authority = ae.getAuthority();
	}

	public AuthEntity getAuthEntity() {
		AuthEntity ae = new AuthEntity();
		ae.setAuthority(this.authority);
		ae.setUsername(this.username);
		ae.setId(this.getId());
		return ae;
	}
	public AuthBean() {		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
