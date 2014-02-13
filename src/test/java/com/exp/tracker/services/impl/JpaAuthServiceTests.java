package com.exp.tracker.services.impl;

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

import com.exp.tracker.data.model.AuthBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.AuthService;
import com.exp.tracker.services.api.UserService;


public class JpaAuthServiceTests extends AbstractExpenseTrackerBaseTest
{

    @Autowired private AuthService authService;
    
    @Autowired private UserService userService;
    private static JdbcDaoImpl userDetailService;
	@Autowired
	private ApplicationContext appContext;
	
    private UserBean userBean;
    private RequestContext rCtx;
    @Before
    public void setup() {
    	userDetailService = appContext.getBean(JdbcDaoImpl.class);
		UserDetails userDetails = userDetailService.loadUserByUsername("Admin");
		Authentication authToken = new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
		rCtx = new MockRequestContext();
		MockExternalContext ec = new MockExternalContext();
        ec.setCurrentUser("Admin");
        ((MockRequestContext) rCtx).setExternalContext(ec);
        // Add a user first, it will add a "ROLE_USER" by default.
        UserBean ub = new UserBean();
        ub.setEmailId("a@b.com");
        ub.setEnabled(true);
        ub.setFirstName("Test");
        ub.setLastName("User");
        ub.setMiddleInit("1");
        ub.setPassword("password");
        ub.setUsername("testuser1");      
        userBean = userService.addUser(ub,rCtx);
    }
    
    @Test
    public void testAuthRemoval() {
        Assert.assertNotNull("There's no user in the DB.", userBean);
        
        for (AuthBean ab : userBean.getAuthSet()) {
            authService.removeAuth(ab.getAuthEntity().getId());
        }
    }
}
