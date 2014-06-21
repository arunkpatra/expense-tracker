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

package com.exp.tracker.flows.viewexpenses;

import java.util.Date;

import org.easymock.EasyMock;
import org.springframework.faces.model.converter.FacesConversionService;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;

import com.exp.tracker.data.model.ExpenseSearchCriteria;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.flows.ExpenseTrackerFlowExecutionTestsParent;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.UserService;

public class ViewExpensesFlowExecutionTests extends
		ExpenseTrackerFlowExecutionTestsParent {

	private UserService userService;
	private ExpenseService expenseService;

	protected void setUp() {
		userService = EasyMock.createMock(UserService.class);
		expenseService = EasyMock.createMock(ExpenseService.class);
	}

	protected void configureFlowBuilderContext(
			MockFlowBuilderContext builderContext) {
		builderContext.registerBean("userService", userService);
		builderContext.registerBean("expenseService", expenseService);
		builderContext.getFlowBuilderServices().setConversionService(
				new FacesConversionService());
	}

	@Override
	protected FlowDefinitionResource getResource(
			FlowDefinitionResourceFactory resourceFactory) {
		return resourceFactory
				.createResource("flows/viewexpenses/viewexpenses-flow.xml");

	}

	public void testStartViewExpensesFlow() {
		UserBean etUser = createTestUserBean();
		EasyMock.expect(userService.getUser("System")).andReturn(etUser);
		EasyMock.replay(userService);

		// ExpenseSearchCriteria expenseSearchCriteria =
		// createTestExpenseSearchCriteria();

		MockExternalContext context = new MockExternalContext();
		context.setCurrentUser("System");

		startFlow(context);

		assertCurrentStateEquals("enterExpenseSearchCriteria");
		assertResponseWrittenEquals("enterExpenseSearchCriteria", context);
		assertTrue(getRequiredFlowAttribute("etUser") instanceof UserBean);
		assertTrue(getRequiredFlowAttribute("expenseSearchCriteria") instanceof ExpenseSearchCriteria);

		EasyMock.verify(userService);
	}

	public void testEnterExpenseSearchCriteria_Search() {
		setCurrentState("enterExpenseSearchCriteria");
		getFlowScope().put("expenseSearchCriteria",
				createTestExpenseSearchCriteria());

		MockExternalContext context = new MockExternalContext();
		context.setEventId("search");
		resumeFlow(context);

		assertCurrentStateEquals("expenseSearchResult");
		assertResponseWrittenEquals("expenseSearchResult", context);
		assertTrue(getRequiredFlowAttribute("expenseSearchCriteria") instanceof ExpenseSearchCriteria);
	}

	private ExpenseSearchCriteria createTestExpenseSearchCriteria() {
		ExpenseSearchCriteria esc = new ExpenseSearchCriteria();
		esc.setEndDate(new Date());
		esc.setStartDate(new Date());
		esc.setPaidBy("System");
		return esc;
	}

	/**
	 * Creates a test user bean.
	 * 
	 * @return userBean
	 */
	private UserBean createTestUserBean() {
		UserBean u = new UserBean();
		u.setId(1L);
		u.setEnabled(true);
		u.setUsername("System");
		u.setPassword("admin");
		u.setPasswordChangeNeeded(true);
		u.setFirstName("System");
		u.setLastName("Administrator");
		u.setId(1L);

		return u;
	}
}
