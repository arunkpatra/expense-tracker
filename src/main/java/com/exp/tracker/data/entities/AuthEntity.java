package com.exp.tracker.data.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="authorities")
@NamedQueries({
	@NamedQuery(name="removeAuthorities", query="DELETE AuthEntity a WHERE a.username = :username"),
	@NamedQuery(name="removeAuthority", query="DELETE AuthEntity a WHERE a.id = :id"),
	@NamedQuery(name="getAuthorities", query="SELECT a FROM AuthEntity a ORDER BY a.authority")})
public class AuthEntity implements Serializable{

	private static final long serialVersionUID = 2817266537467589269L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private String username;	
	private String authority;
	private Long user_id;
	
	@ManyToOne (cascade=CascadeType.REMOVE)
	@JoinColumn(name="user_id", referencedColumnName="id", insertable=false, updatable=false)
	private UserEntity user;
	
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long userId) {
		user_id = userId;
	}
}
