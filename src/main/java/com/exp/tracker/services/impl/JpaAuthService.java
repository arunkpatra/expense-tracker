package com.exp.tracker.services.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exp.tracker.data.entities.AuthEntity;
import com.exp.tracker.services.api.AuthService;

@Service("authService")
@Repository
public class JpaAuthService implements AuthService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Transactional
	public void removeAuth(Long id) {
		AuthEntity ae = em.find(AuthEntity.class, id);
		em.remove(ae);
		em.flush();
	}

}
