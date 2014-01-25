package com.exp.tracker.services.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;

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
import org.springframework.web.context.WebApplicationContext;

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.ReportGenerationService;
import com.exp.tracker.services.api.SettlementService;
import com.exp.tracker.services.api.UserService;

public class JasperReportGenerationServiceTests extends
		AbstractExpenseTrackerBaseTest {

	private static JdbcDaoImpl userDetailService;
	private ExpenseDetail expenseDetail;

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private ReportGenerationService reportService;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private UserService userService;

	@Autowired
	private SettlementService settlementService;

	private ServletContext context;

	@Before
	public void setup() {
		// Sanity check
		Assert.assertTrue("Expected an WebApplicationContext",
				appContext instanceof WebApplicationContext);
		WebApplicationContext ctx = (WebApplicationContext) appContext;
		context = ctx.getServletContext();

		// Set current user
		userDetailService = appContext.getBean(JdbcDaoImpl.class);
		UserDetails userDetails = userDetailService.loadUserByUsername("Admin");
		Authentication authToken = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		// Create two users
		// Add 1st user
		UserBean ub1 = new UserBean();
		ub1.setEmailId("a@b.com");
		ub1.setEnabled(true);
		ub1.setFirstName("Test1");
		ub1.setLastName("User1");
		ub1.setMiddleInit("1");
		ub1.setPassword("password");
		ub1.setUsername("reptusr1");
		UserBean userBean1 = userService.addUser(ub1);
		Assert.assertNotNull("Failed to create user1.", userBean1);
		//
		// Add 2nd user
		UserBean ub2 = new UserBean();
		ub2.setEmailId("a@b.com");
		ub2.setEnabled(true);
		ub2.setFirstName("Test2");
		ub2.setLastName("User2");
		ub2.setMiddleInit("2");
		ub2.setPassword("password");
		ub2.setUsername("reptusr2");
		UserBean userBean2 = userService.addUser(ub2);
		Assert.assertNotNull("Failed to create user2", userBean2);

		// Setup an expense
		ExpenseDetail ed = new ExpenseDetail();
		ed.setAmount(20.0F);
		ed.setCategory("Somecategory");
		ed.setCreatedBy("Admin");
		ed.setDate(new Date());
		ed.setDescription("Some Expense");
		ed.setPaidBy("reptusr1");
		ed.setSettlementId(null);
		// now set shares
		UserShare us1 = new UserShare("reptusr1", 10.0F, 0.0F, true);
		UserShare us2 = new UserShare("reptusr2", 10.0F, 0.0F, true);
		ed.getUserShares().add(us1);
		ed.getUserShares().add(us2);
		//
		expenseDetail = ed;
	}

	@Test
	public void reportGenerationTests() {
		// Save expense
		Assert.assertNotNull("Expense detail is null", expenseDetail);
		int result = expenseService.saveExpense(expenseDetail);
		Assert.assertTrue("Failed to save expense.", result == 0);

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
		Long sid = settlementService.createSettlement(sb);
		Assert.assertTrue("Failed to create settlement", sid != 0L);

		// Get report paths
		String settlementReportTemplatePath = context
				.getRealPath(JasperReportGenerationService.SETTLEMENT_REPORT_FILE_NAME);
		String expenseReportTemplatePath = context
				.getRealPath(JasperReportGenerationService.EXPENSE_REPORT_FILE_NAME);
		// get settlement report
		byte[] srBytes = reportService.genSettlementReportInternal(sid,
				settlementReportTemplatePath);
		Assert.assertNotNull("Failed to create settlement report", srBytes);
		Assert.assertTrue("Empty settlement report", srBytes.length != 0);
		byte[] erBytes = reportService.genExpenseReportInternal(sid,
				expenseReportTemplatePath);
		Assert.assertNotNull("Failed to create expense report", erBytes);
		Assert.assertTrue("Empty expense report", erBytes.length != 0);

	}
}
