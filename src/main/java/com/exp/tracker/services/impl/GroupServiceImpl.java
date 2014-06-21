package com.exp.tracker.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
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
    public void setEntityManager(EntityManager em)
    {
        this.em = em;
    }
    
	@Override
	@Transactional(readOnly = true)
	public Collection<GroupBean> getGroups() {
		List<GroupBean> groupList = new ArrayList<GroupBean>();
		Query queryGetAllGroups = em.createNamedQuery("getAllGroups");
		List<GroupEntity> gl = queryGetAllGroups.getResultList();
        for (GroupEntity ge : gl) {
        	groupList.add(new GroupBean(ge));
        }
		return groupList;
	}
	

}
