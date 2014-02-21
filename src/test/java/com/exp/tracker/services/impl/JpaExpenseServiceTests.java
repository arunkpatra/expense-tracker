package com.exp.tracker.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
//import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.mapping.MappingResult;
import org.springframework.binding.mapping.MappingResults;
import org.springframework.binding.mapping.impl.DefaultMappingResults;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockRequestContext;
import org.springframework.webflow.validation.DefaultValidationContext;

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.ExpenseSearchCriteria;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.UserService;
public class JpaExpenseServiceTests extends AbstractExpenseTrackerBaseTest
{

	static JdbcDaoImpl userDetailService;
	
	@Autowired private ExpenseService expenseService;
	
	@Autowired private UserService userService;
	
	@Autowired
	ApplicationContext ctx;
	private RequestContext rCtx;
	private ExpenseDetail expenseDetail;
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
        ub1.setUsername("testuser1");      
        UserBean userBean1 = userService.addUser(ub1,rCtx);
        Assert.assertNotNull("Failed to create user1. Why", userBean1);
        //
        // Add 1st user
        UserBean ub2 = new UserBean();
        ub2.setEmailId("a@b.com");
        ub2.setEnabled(true);
        ub2.setFirstName("Test2");
        ub2.setLastName("User2");
        ub2.setMiddleInit("2");
        ub2.setPassword("password");
        ub2.setUsername("testuser2");      
        UserBean userBean2 = userService.addUser(ub2,rCtx);
        Assert.assertNotNull("Failed to create user2", userBean2);
        // Setup an expense
		ExpenseDetail ed = new ExpenseDetail();
		ed.setAmount(20.0F);
		ed.setCategory("Somecategory");
		ed.setCreatedBy("Admin");
		ed.setDate(new Date());
		ed.setDescription("Some Expense");
		ed.setPaidBy("testuser1");
		ed.setSettlementId(null);
		// now set shares
		UserShare us1 = new UserShare("testuser1", 10.0F, 0.0F, true);
		UserShare us2 = new UserShare("testuser2", 10.0F, 0.0F, true);
		ed.getUserShares().add(us1);
		ed.getUserShares().add(us2);
		//
		expenseDetail = ed;
		
	}
	
	@Test
	public void expenseRelatedTests() {
		// Check if expense was saved earlier
		Assert.assertNotNull("Expense detail is null",expenseDetail);
		
		// start
		//RequestContext requestContext = new MockRequestContext();//MockRequestContext();
		//
//		MockExternalContext ec = new MockExternalContext();
//        ec.setCurrentUser("Admin");
//        ((MockRequestContext) requestContext).setExternalContext(ec);
        
		MappingResults mResults = new DefaultMappingResults(null, null, new ArrayList<MappingResult>());
		ValidationContext vc = new DefaultValidationContext(rCtx, "calcShares", mResults);
		expenseDetail.validateEnterExpenseDetail(vc);
		// try with next
		vc = new DefaultValidationContext(rCtx, "next", mResults);
		expenseDetail.setOverrideSharesFlag(true);
        expenseDetail.validateEnterExpenseDetail(vc);
        
		// end
		int result = expenseService.saveExpense(expenseDetail);
		Assert.assertTrue("Failed to save expense.", result == 0);
		
		// Search for expenses in a date range
		ExpenseSearchCriteria esc = new ExpenseSearchCriteria();
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
		//
		esc.setEndDate(tomorrow);
		esc.setStartDate(yesterday);
		List<ExpenseDetail> expenses =  expenseService.getExpenses(esc);
		Assert.assertNotNull("Failed to get expenses", expenses);
		int i = expenses.size();
		Assert.assertTrue("Expected exactly 1 expense.", i == 1);
		
		// Locate a specific expense by id
		ExpenseDetail ee = expenses.get(0);
		ExpenseDetail ed = expenseService.getExpenseById(ee.getId());
		Assert.assertNotNull("Failed to get expensedetail", ed);
		
		// Edit record
		ed.setEditMode(true);
		ed.setCategory("New category");
		int resultR = expenseService.saveExpense(ed);
		Assert.assertTrue("Failed to edit expense.", resultR == 0);
				
		// Locate expense detail bean
		ExpenseDetail ed1 = expenseService.getExpenseDetailBeanById(ee.getId());
		Assert.assertNotNull("Failed to get expensedetailbean", ed1);
		
		// Get unsettled expenses
		SettlementBean sb = new SettlementBean();
		sb.setStartDate(yesterday);
		sb.setEndDate(tomorrow);
		List<ExpenseDetail> ex2 = expenseService.getUnsettledExpenses(sb);
		Assert.assertNotNull("Failed to get unsettled expenses", ex2);
		Assert.assertTrue("Expected exactly 1 expense to settle.", ex2.size() == 1);
		
		// Delete expense
		int delresult = expenseService.deleteExpenseById(ee.getId(), rCtx);
		Assert.assertTrue("Failed to delete expense", delresult == 0);
		
		// get expense for null user id
		expenseService.getExpenseDetailBeanById(null);
	}
}
