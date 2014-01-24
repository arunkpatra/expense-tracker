package com.exp.tracker.services.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/root-applicationContext.xml")
@ActiveProfiles("demo")
@WebAppConfiguration
public class JpaExpenseServiceTests
{

	@Autowired private ExpenseService expenseService;
	@Autowired private UserService userService;
	
	private ExpenseDetail expenseDetail;
	@Before
    public void setup() {
		// Add 1st user
        UserBean ub1 = new UserBean();
        ub1.setEmailId("a@b.com");
        ub1.setEnabled(true);
        ub1.setFirstName("Test1");
        ub1.setLastName("User1");
        ub1.setMiddleInit("1");
        ub1.setPassword("password");
        ub1.setUsername("testuser1");      
        UserBean userBean1 = userService.addUser(ub1);
        Assert.assertNotNull("Failed to create user1", userBean1);
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
        UserBean userBean2 = userService.addUser(ub2);
        Assert.assertNotNull("Failed to create user2", userBean2);
        //
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
    public void testSaveExpense() {
		Assert.assertNotNull("Expense detail is null",expenseDetail);
		int result = expenseService.saveExpense(expenseDetail);
		Assert.assertTrue("Failed to save expense.", result == 0);
	}
}
