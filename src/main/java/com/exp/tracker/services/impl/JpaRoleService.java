package com.exp.tracker.services.impl;

import java.util.ArrayList;
import java.util.Collection;
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
		Collection roles = queryGetRoles.getResultList();
		List<RoleEntity> roleList = new ArrayList<RoleEntity>(roles);
		return roleList;
	}

	public List<SelectItem> getRolesSelectItems() {

		List<SelectItem> rl = new ArrayList<SelectItem>();
		for (RoleEntity re : getRoles()) {
			rl.add(new SelectItem(re.getRole(),re.getRole()));			 
		}		
		return rl;
	}
}
