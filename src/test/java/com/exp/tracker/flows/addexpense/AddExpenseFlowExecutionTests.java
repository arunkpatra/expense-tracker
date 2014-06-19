package com.exp.tracker.flows.addexpense;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.springframework.faces.model.converter.FacesConversionService;
import org.springframework.webflow.config.FlowDefinitionResource;
import org.springframework.webflow.config.FlowDefinitionResourceFactory;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockFlowBuilderContext;

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.flows.ExpenseTrackerFlowExecutionTestsParent;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.UserService;

public class AddExpenseFlowExecutionTests extends
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
				.createResource("flows/addexpense/addexpense-flow.xml");
	}

	public void testStartAddExpenseFlow() {
		UserBean etUser = createTestUserBean();
		EasyMock.expect(userService.getUser("System")).andReturn(etUser);
		EasyMock.replay(userService);

		ExpenseDetail expenseDetail = createTestExpenseDetail();
		EasyMock.expect(expenseService.getExpenseDetailBeanById(null))
				.andReturn(expenseDetail);
		EasyMock.replay(expenseService);

		MockExternalContext context = new MockExternalContext();
		context.setCurrentUser("System");

		startFlow(context);

		assertCurrentStateEquals("enterExpenseDetail");
		assertResponseWrittenEquals("enterExpenseDetail", context);
		assertTrue(getRequiredFlowAttribute("etUser") instanceof UserBean);
		assertTrue(getRequiredFlowAttribute("expenseDetail") instanceof ExpenseDetail);

		EasyMock.verify(userService);
		EasyMock.verify(expenseService);
	}

	public void testEnterExpenseDetails_CalcShares() {
		setCurrentState("enterExpenseDetail");
		getFlowScope().put("expenseDetail", createTestExpenseDetail());

		MockExternalContext context = new MockExternalContext();
		context.setEventId("calcShares");
		resumeFlow(context);

		assertCurrentStateEquals("enterExpenseDetail");
		assertResponseWrittenEquals("enterExpenseDetail", context);
		assertTrue(getRequiredFlowAttribute("expenseDetail") instanceof ExpenseDetail);
	}

	public void testEnterExpenseDetails_OverrideShares() {
		setCurrentState("enterExpenseDetail");
		getFlowScope().put("expenseDetail", createTestExpenseDetail());

		MockExternalContext context = new MockExternalContext();
		context.setEventId("overrideShares");
		resumeFlow(context);

		assertCurrentStateEquals("enterExpenseDetail");
		assertResponseWrittenEquals("enterExpenseDetail", context);
		assertTrue(getRequiredFlowAttribute("expenseDetail") instanceof ExpenseDetail);
	}

	public void testEnterExpenseDetails_Next() {
		setCurrentState("enterExpenseDetail");
		getFlowScope().put("expenseDetail", createTestExpenseDetail());
		getFlowScope().put("userShares",
				createTestExpenseDetail().getUserShares());
		MockExternalContext context = new MockExternalContext();
		context.setEventId("next");
		resumeFlow(context);

		assertCurrentStateEquals("reviewExpense");
		assertResponseWrittenEquals("reviewExpense", context);
		assertTrue(getRequiredFlowAttribute("userShares") instanceof List<?>);
	}

	public void testReviewExpense_SaveExpense() {

		setCurrentState("reviewExpense");
		getFlowScope().put("userShares",
				createTestExpenseDetail().getUserShares());
		ExpenseDetail expenseDetail = createTestExpenseDetail();
		getFlowScope().put("expenseDetail", expenseDetail);
		EasyMock.expect(expenseService.saveExpense(expenseDetail)).andReturn(
				new Integer(0));
		EasyMock.replay(expenseService);

		MockExternalContext context = new MockExternalContext();
		context.setEventId("saveExpense");
		resumeFlow(context);

		assertCurrentStateEquals("expenseAdditionResult");
		assertResponseWrittenEquals("expenseAdditionResult", context);

	}

	private ExpenseDetail createTestExpenseDetail() {
		ExpenseDetail ed = new ExpenseDetail();
		ed.setId(1L);
		ed.setAmount(20.0F);
		ed.setCategory("Some Category");
		ed.setCreatedBy("System");
		ed.setDate(new Date());
		ed.setDescription("Some Description");
		ed.setPaidBy("System");
		ed.setSettlementId(null);

		List<UserShare> usList = new ArrayList<UserShare>();
		UserShare us1 = new UserShare("System", 2.0F, 20.0F, true);
		usList.add(us1);
		UserShare us2 = new UserShare("Tom", 4.5F, 30.0F, false);
		usList.add(us2);
		UserShare us3 = new UserShare("John", 3.0F, 40.0F, true);
		usList.add(us3);

		ed.setUserShares(usList);
		return ed;

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
