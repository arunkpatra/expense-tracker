package com.exp.tracker.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.PaymentBean;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.data.model.UserSettlementBean;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.PaymentService;
import com.exp.tracker.services.api.SettlementService;
import com.exp.tracker.services.api.UserService;

public class JpaSettlementServiceTests extends AbstractExpenseTrackerBaseTest {

	static JdbcDaoImpl userDetailService;

	@Autowired
	ApplicationContext ctx;
	@Autowired
	private ExpenseService expenseService;
	@Autowired
	private UserService userService;
	@Autowired
	private SettlementService settlementService;
	@Autowired
	PaymentService paymentService;
	private RequestContext rCtx;
	@Before
	public void setup() {
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
		// Add 1st user
		UserBean ub1 = new UserBean();
		ub1.setEmailId("a@b.com");
		ub1.setEnabled(true);
		ub1.setFirstName("Test1");
		ub1.setLastName("User1");
		ub1.setMiddleInit("1");
		ub1.setPassword("password");
		ub1.setUsername("testuser3");
		UserBean userBean1 = userService.addUser(ub1,rCtx);
		Assert.assertNotNull("Failed to create user3. Why Why", userBean1);
		//
		// Add 1st user
		UserBean ub2 = new UserBean();
		ub2.setEmailId("a@b.com");
		ub2.setEnabled(true);
		ub2.setFirstName("Test2");
		ub2.setLastName("User2");
		ub2.setMiddleInit("2");
		ub2.setPassword("password");
		ub2.setUsername("testuser4");
		UserBean userBean2 = userService.addUser(ub2,rCtx);
		Assert.assertNotNull("Failed to create user4", userBean2);
		//
		ExpenseDetail ed = new ExpenseDetail();
		ed.setAmount(20.0F);
		ed.setCategory("Somecategory");
		ed.setCreatedBy("Admin");
		ed.setDate(new Date());
		ed.setDescription("Some Expense");
		ed.setPaidBy("testuser3");
		ed.setSettlementId(null);
		// now set shares
		UserShare us1 = new UserShare("testuser3", 10.0F, 0.0F, true);
		UserShare us2 = new UserShare("testuser4", 10.0F, 0.0F, true);
		ed.getUserShares().add(us1);
		ed.getUserShares().add(us2);

		Assert.assertNotNull("Expense detail is null", ed);
		int result = expenseService.saveExpense(ed);
		Assert.assertTrue("Failed to save expense.", result == 0);
	}

	@Test
	public void settlementServiceTests() {

        
		// Create Settlement
		Date today = new Date();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(today);
		cal1.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = cal1.getTime();
		//
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(today);
		cal2.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = cal2.getTime();
		SettlementBean sb = new SettlementBean();
		sb.setStartDate(yesterday);
		sb.setEndDate(tomorrow);
		// Persist object
		Long result = settlementService.createSettlement(sb,rCtx);
		Assert.assertTrue("Failed to create settlement", result != 0L);
		// get it back
		SettlementBean slb = settlementService.getSettlementById(result);
		Assert.assertNotNull("Failed to retrieve settlement", slb);
		// Find all settlements
		List<SettlementBean> sList = settlementService.getSettlements();
		Assert.assertTrue("Expected exactly 1 settlement.", sList.size() == 1);
		// Try to close settlement, it should fail
		int completionResult = settlementService.completeSettlement(result);
		Assert.assertTrue("Expected settlement closure to fail.",
				completionResult == 1);
		// Try to delete it, it should fail
		// int deletionResult = settlementService.deleteSettlement(result);
		// Assert.assertTrue("Expected deletion to fail.", deletionResult == 1);
		// try applying payments
		for (UserSettlementBean usb : slb.getUserSettlementList()) {
			paymentService.applyUserPayment(usb.getId());
		}
		// Get some payments
		List<PaymentBean> pbList = paymentService.getAllPayments();
		Assert.assertNotNull("Payments list", pbList);
		// Get payment for user
		List<PaymentBean> pbList2 = paymentService.getPaymnentsForUser("testuser3");
		Assert.assertNotNull("Payments list for user testuser3 is null", pbList2);
		
		// Now try to close again
		int completionResult2 = settlementService.completeSettlement(result);
		Assert.assertTrue("Settlement closure should have succeded.",
				completionResult2 == 0);
		// now delete settlement
		int deletionResult = settlementService.deleteSettlement(result);
		Assert.assertTrue("Expected deletion to succeed.", deletionResult == 0);
	}
}
