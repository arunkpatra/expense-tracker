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

package com.exp.tracker.flows.changepassword;

import org.easymock.EasyMock;
import org.springframework.faces.model.converter.FacesConversionService;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;

import com.exp.tracker.data.model.PasswordChangeBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.flows.ExpenseTrackerFlowExecutionTestsParent;
import com.exp.tracker.services.api.UserService;

public class ChangePasswordFlowExecutionTests extends
		ExpenseTrackerFlowExecutionTestsParent {
	private UserService userService;

	protected void setUp() {
		userService = EasyMock.createMock(UserService.class);
	}

	@Override
	protected FlowDefinitionResource getResource(
			FlowDefinitionResourceFactory resourceFactory) {
		return resourceFactory
				.createResource("flows/changepassword/changepassword-flow.xml");

	}

	protected void configureFlowBuilderContext(
			MockFlowBuilderContext builderContext) {
		builderContext.registerBean("userService", userService);
		builderContext.getFlowBuilderServices().setConversionService(
				new FacesConversionService());
	}

	public void testStartChangePasswordFlow() {
		UserBean etUser = createTestUserBean();
		UserBean currentUserBean = createTestUserBean();

		EasyMock.expect(userService.getUser("System")).andReturn(etUser);
		EasyMock.expect(userService.getUser("System")).andReturn(
				currentUserBean);

		EasyMock.replay(userService);

		MockExternalContext context = new MockExternalContext();
		context.setCurrentUser("System");

		startFlow(context);

		assertCurrentStateEquals("changePasswordEntry");
		assertResponseWrittenEquals("changePasswordEntry", context);
		assertTrue(getRequiredFlowAttribute("etUser") instanceof UserBean);
		assertTrue(getRequiredFlowAttribute("currentUserBean") instanceof UserBean);

		EasyMock.verify(userService);
	}

//	public void testChangePasswordEntry_Save() {
//		setCurrentState("changePasswordEntry");
//		PasswordChangeBean passwordChangeBean = new PasswordChangeBean();
//		UserBean currentUserBean = createTestUserBean();
//		getFlowScope().put("passwdChangeBean", passwordChangeBean);
//		getFlowScope().put("currentUserBean", currentUserBean);
//
//		MockExternalContext context = new MockExternalContext();
//		context.setEventId("save");
//		resumeFlow(context);
//
//		assertCurrentStateEquals("passwordChangeResults");
//		assertResponseWrittenEquals("passwordChangeResults", context);
//
//		context.setEventId("ok");
//		resumeFlow(context);
//		assertFlowExecutionEnded();
//	}

	public void testChangePasswordEntry_Cancel() {
		setCurrentState("changePasswordEntry");
		PasswordChangeBean passwordChangeBean = new PasswordChangeBean();
		getFlowScope().put("passwdChangeBean", passwordChangeBean);

		MockExternalContext context = new MockExternalContext();
		context.setEventId("cancel");
		resumeFlow(context);

		assertFlowExecutionEnded();
	}

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
