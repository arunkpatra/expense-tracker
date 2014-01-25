package com.exp.tracker.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.exp.tracker.data.model.AuthBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.AuthService;
import com.exp.tracker.services.api.UserService;


public class JpaAuthServiceTests extends AbstractExpenseTrackerBaseTest
{

    @Autowired private AuthService authService;
    
    @Autowired private UserService userService;
    
    private UserBean userBean;
    
    @Before
    public void setup() {
        // Add a user first, it will add a "ROLE_USER" by default.
        UserBean ub = new UserBean();
        ub.setEmailId("a@b.com");
        ub.setEnabled(true);
        ub.setFirstName("Test");
        ub.setLastName("User");
        ub.setMiddleInit("1");
        ub.setPassword("password");
        ub.setUsername("testuser1");      
        userBean = userService.addUser(ub);
    }
    
    @Test
    public void testAuthRemoval() {
        Assert.assertNotNull("There's no user in the DB.", userBean);
        
        for (AuthBean ab : userBean.getAuthSet()) {
            authService.removeAuth(ab.getAuthEntity().getId());
        }
    }
}
