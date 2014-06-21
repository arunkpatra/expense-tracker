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

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.Message;
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
import com.exp.tracker.services.api.ServiceOperationValidationService;

public class GroupServiceImplTests extends AbstractExpenseTrackerBaseTest {

	@Autowired
	private GroupService groupService;

	@Autowired
	private ServiceOperationValidationService validationService;

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
		//
		validationService.checkGroupCreationResult(retGb, rCtx);
		Message[] msgs = rCtx.getMessageContext().getAllMessages();
		Assert.assertTrue("Expected one message", msgs.length == 1);
		Message msg = msgs[0];
		Assert.assertTrue("Expected a success message",
				"Your group was created succesfuly.".equals(msg.getText()));
		// clear this message
		rCtx.getMessageContext().clearMessages();
		// now try adding group again with same name
		retGb = groupService.addGroup(gb);
		Assert.assertNull("Should have failed to create group", retGb);
		validationService.checkGroupCreationResult(retGb, rCtx);
		msgs = rCtx.getMessageContext().getAllMessages();
		Assert.assertTrue("Expected one message", msgs.length == 1);
		msg = msgs[0];
		Assert.assertTrue("Expected a failure message",
				"Failed to create group.".equals(msg.getText()));
	}
}
