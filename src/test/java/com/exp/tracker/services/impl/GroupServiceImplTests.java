package com.exp.tracker.services.impl;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockRequestContext;

import com.exp.tracker.data.model.GroupBean;
import com.exp.tracker.services.api.GroupService;

public class GroupServiceImplTests extends AbstractExpenseTrackerBaseTest {

	@Autowired
	private GroupService groupService;
	
	@Autowired
	ApplicationContext ctx;
	private RequestContext rCtx;
	
	static JdbcDaoImpl userDetailService;
	
	@Before
	public void setup() {

	}
	
	@Test
	public void groupServiceTests() {
		userDetailService = ctx.getBean(JdbcDaoImpl.class);
		UserDetails userDetails = userDetailService.loadUserByUsername("Admin");
		Authentication authToken = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
		rCtx = new MockRequestContext();
		MockExternalContext ec = new MockExternalContext();
		ec.setCurrentUser("Admin");
		((MockRequestContext) rCtx).setExternalContext(ec);
		
		// This test runs with two groups already setup in the embedded test db
		
		// get all groups
		Collection<GroupBean> groups = groupService.getGroups();
		Assert.assertNotNull("Expected a non null group list", groups);
		Assert.assertTrue("Expected exactly two groups", groups.size() == 2);
		
		// Add a group
		GroupBean gb = new GroupBean();
		gb.setActive(true);
		gb.setGroupDescription("Test Description");
		gb.setGroupName("Test group name");
		gb.setUsers(null);
		GroupBean retGb = groupService.addGroup(gb);
		Assert.assertNotNull("Failed to create group", retGb);
		groups = groupService.getGroups();
		Assert.assertTrue("Expected exactly three groups", groups.size() == 3);
		
	}
}
