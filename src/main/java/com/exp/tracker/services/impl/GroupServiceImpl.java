package com.exp.tracker.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exp.tracker.data.entities.GroupEntity;
import com.exp.tracker.data.model.GroupBean;
import com.exp.tracker.services.api.GroupService;

@Service("groupService")
@Repository
public class GroupServiceImpl implements GroupService {

	/**
	 * The logger.
	 */
	private static final Log logger = LogFactory.getLog(GroupServiceImpl.class);

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<GroupBean> getGroups() {
		List<GroupBean> groupList = new ArrayList<GroupBean>();
		Query queryGetAllGroups = em.createNamedQuery("getAllGroups");
		@SuppressWarnings("unchecked")
		List<GroupEntity> gl = queryGetAllGroups.getResultList();
		for (GroupEntity ge : gl) {
			groupList.add(new GroupBean(ge));
		}
		return groupList;
	}

	@Override
	@Transactional
	public GroupBean addGroup(GroupBean group) {
		GroupBean newGb = null;
		boolean groupExists = true;
		// first find match
		Query queryFindMatch = em.createNamedQuery("findGroupMatch");
		queryFindMatch.setParameter("groupName", group.getGroupName());
		@SuppressWarnings("unused")
		GroupEntity ge = null;
		try {
			ge = (GroupEntity) queryFindMatch.getSingleResult();
		} catch (NoResultException nre) {
			if (logger.isDebugEnabled()) {
				logger.debug("Group does not exist, thus can be added - "
						+ group.getGroupName());
			}
			groupExists = false;
		}
		if (!groupExists) {
			GroupEntity ge1 = group.getGroupEntity();
			Calendar calendar = Calendar.getInstance();
			ge1.setCreationDate(calendar.getTime());
			ge1.setLastUpdatedDate(calendar.getTime());

			em.persist(ge1);
			if (logger.isDebugEnabled()) {
				logger.debug("Group added succesfuly - "
						+ group.getGroupName());
			}
			newGb = new GroupBean(ge1);
		}
		return newGb;
	}
}
