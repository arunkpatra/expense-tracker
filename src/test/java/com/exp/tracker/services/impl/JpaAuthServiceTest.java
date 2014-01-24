package com.exp.tracker.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.exp.tracker.data.model.AuthBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.AuthService;
import com.exp.tracker.services.api.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/root-applicationContext.xml")
@ActiveProfiles("demo")
@WebAppConfiguration
public class JpaAuthServiceTest
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
