package com.exp.tracker.services.impl;

import java.util.Collection;
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

import com.exp.tracker.data.entities.UserEntity;
import com.exp.tracker.data.model.AuthBean;
import com.exp.tracker.data.model.PasswordChangeBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.EmailService;
import com.exp.tracker.services.api.UserService;

public class JpaUserServiceTests extends AbstractExpenseTrackerBaseTest {

	@Autowired
	private UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	ApplicationContext ctx;

	static JdbcDaoImpl userDetailService;
	private RequestContext rCtx;

	@Before
	public void setup() {

	}

	@Test
	public void userServiceTests() {

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

		// add a user
		UserBean ub1 = new UserBean();
		ub1.setEmailId("a@b.com");
		ub1.setEnabled(true);
		ub1.setFirstName("Test1");
		ub1.setLastName("User1");
		ub1.setMiddleInit("1");
		ub1.setPassword("password");
		ub1.setUsername("ustest1");
		// add auth
		// ub1.addAuth("ROLE_SITE_ADMIN");
		// add again
		// ub1.addAuth("ROLE_SITE_ADMIN");
		UserBean userBean1 = userService.addUser(ub1, rCtx);
		Assert.assertNotNull("Failed to create ustest1. Why", userBean1);
		// send mail
		emailService.sendWelcomeEmail(userBean1);
		// try to add again
		UserBean userBean2 = userService.addUser(ub1, rCtx);
		Assert.assertNull("Should not have created duplicate user", userBean2);
		// Get users
		List<UserEntity> ueList = userService.getUsers();
		Assert.assertNotNull("Should have got users", ueList);
		// Note that when the embedded db is initialized in tests, one admin
		// user is created : Admin/password
		Assert.assertTrue("Expected at least 2 users.", ueList.size() >= 2);
		// Get user name list
		List<String> unList = userService.getUserNames();
		Assert.assertNotNull("Obtained null list of user names", unList);
		Assert.assertTrue("Expected at least 2 user name in list",
				unList.size() >= 2);
		// Get my user
		UserBean myUser = userService.getUser("ustest1");
		Assert.assertNotNull("Failed to retrieve user by name", myUser);
		// Change password
		PasswordChangeBean pcb = new PasswordChangeBean();
		pcb.setOldPassword("password");
		pcb.setNewPassword("catanddog");
		pcb.setNewPasswordAgain("catanddog");
		String result = userService.changePassword(pcb, myUser);
		Assert.assertTrue("Password change should have succeded",
				"".equalsIgnoreCase(result));
		// send email
		emailService.sendPasswordResetEmail(myUser);
		// Fail this time
		pcb.setOldPassword("catanddog");
		pcb.setNewPassword("tiger");
		pcb.setNewPasswordAgain("tigeragain");
		Assert.assertTrue("Password change failed",
				"New passwords do not match.".equalsIgnoreCase(userService
						.changePassword(pcb, myUser)));
		// Fail again
		pcb.setOldPassword("tiger");
		pcb.setNewPassword("lion");
		pcb.setNewPasswordAgain("lion");
		Assert.assertTrue("Password change failed", "Old password is invalid."
				.equalsIgnoreCase(userService.changePassword(pcb, myUser)));

		// reset password of the user

		userService.resetPassword("ustest1", rCtx);
		// Is password change needed
		Assert.assertTrue(
				"User is supposed to change password after reset by admin",
				userService.isPasswordChangeNeeded("ustest1"));
		// Update user
		myUser.setEmailId("d@g.com");
		userService.updateUser(myUser, rCtx);
		// Get user name select items
		Assert.assertNotNull("Expected user select items",
				userService.getUserNamesSelectItems());
		// Update authorization
		Assert.assertNotNull("Update auth failed",
				userService.updateAutorization(myUser, rCtx));
		// delete user
		int result2 = userService.deleteUser(myUser.getId(), "Admin", rCtx);
		Assert.assertTrue("Failed to delete user", result2 == 0);
		// clear user data
		userBean1.clearUserData();
		// remove Auths
		for (AuthBean ab : myUser.getAuthSet()) {
			userService.removeAuthById(ab.getAuthEntity().getId(), rCtx);
		}
		myUser.getAuthSet();
		// get user beans
		Collection<UserBean> ubs = userService.getUserBeans();
		Assert.assertNotNull("Failed to get user beans", ubs);
	}
}
