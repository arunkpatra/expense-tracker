package com.exp.tracker.services.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockRequestContext;

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.services.api.SettlementService;
import com.exp.tracker.services.api.UserService;

public class ReportControllerTests extends AbstractExpenseTrackerBaseTest
{
    static JdbcDaoImpl userDetailService;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private SettlementService settlementService;
    RequestContext rCtx;
    @Before
    public void setup()
    {
    	userDetailService = wac.getBean(JdbcDaoImpl.class);
        UserDetails userDetails = userDetailService.loadUserByUsername("Admin");
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        rCtx = new MockRequestContext();
		MockExternalContext ec = new MockExternalContext();
        ec.setCurrentUser("Admin");
        ((MockRequestContext) rCtx).setExternalContext(ec);
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        //
        // Add 1st user
        UserBean ub1 = new UserBean();
        ub1.setEmailId("a@b.com");
        ub1.setEnabled(true);
        ub1.setFirstName("Test1");
        ub1.setLastName("User1");
        ub1.setMiddleInit("1");
        ub1.setPassword("password");
        ub1.setUsername("testuserX");
        UserBean userBean1 = userService.addUser(ub1,rCtx);
        Assert.assertNotNull("Failed to create userX. Why Why", userBean1);
        //
        // Add 1st user
        UserBean ub2 = new UserBean();
        ub2.setEmailId("a@b.com");
        ub2.setEnabled(true);
        ub2.setFirstName("Test2");
        ub2.setLastName("User2");
        ub2.setMiddleInit("2");
        ub2.setPassword("password");
        ub2.setUsername("testuserY");
        UserBean userBean2 = userService.addUser(ub2,rCtx);
        Assert.assertNotNull("Failed to create userY", userBean2);
        //
        ExpenseDetail ed = new ExpenseDetail();
        ed.setAmount(20.0F);
        ed.setCategory("Somecategory");
        ed.setCreatedBy("Admin");
        ed.setDate(new Date());
        ed.setDescription("Some Expense");
        ed.setPaidBy("testuserX");
        ed.setSettlementId(null);
        // now set shares
        UserShare us1 = new UserShare("testuserX", 10.0F, 0.0F, true);
        UserShare us2 = new UserShare("testuserY", 10.0F, 0.0F, true);
        ed.getUserShares().add(us1);
        ed.getUserShares().add(us2);

        Assert.assertNotNull("Expense detail is null", ed);
        int result = expenseService.saveExpense(ed);
        Assert.assertTrue("Failed to save expense.", result == 0);
    }

    @Test
    public void getReport() throws Exception
    {
        
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
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/reports/settlementSubReport.pdf?sid=" + result))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/reports/expenseReport.pdf?sid=" + result))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
