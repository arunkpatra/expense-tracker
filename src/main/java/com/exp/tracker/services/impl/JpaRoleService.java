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

package com.exp.tracker.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.exp.tracker.data.entities.RoleEntity;
import com.exp.tracker.services.api.RoleService;

@Service("roleService")
@Repository
public class JpaRoleService implements RoleService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleEntity> getRoles() {
		Query queryGetRoles = em.createNamedQuery("getRoles");		
		return queryGetRoles.getResultList();
	}

	public List<SelectItem> getRolesSelectItems() {

		List<SelectItem> rl = new ArrayList<SelectItem>();
		for (RoleEntity re : getRoles()) {
			rl.add(new SelectItem(re.getRole(),re.getRole()));			 
		}		
		return rl;
	}
}
