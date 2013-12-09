package com.exp.tracker.data.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="roles")
@NamedQueries({
@NamedQuery(name="getRoles", query="SELECT r FROM RoleEntity r "),
@NamedQuery(name="getRoleByName", query="SELECT r FROM RoleEntity r WHERE r.role = :role")})
public class RoleEntity implements Serializable{
	private static final long serialVersionUID = 5358945972128736040L;
	public static final String ROLE_USER ="ROLE_USER";
	public static final String ROLE_SUPERVISOR ="ROLE_SUPERVISOR";
	public static final String ROLE_SITE_ADMIN ="ROLE_SITE_ADMIN";
	
	@Id
	private Long id;	
	private String role;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
