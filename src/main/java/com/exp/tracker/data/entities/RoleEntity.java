/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
