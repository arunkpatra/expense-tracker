package com.exp.tracker.services.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.exp.tracker.data.entities.UserEntity;
import com.exp.tracker.data.model.PasswordChangeBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.UserService;

public class JpaUserServiceTests extends AbstractExpenseTrackerBaseTest {

	@Autowired
	private UserService userService;

	@Before
	public void setup() {

	}

	@Test
	public void userServiceTests() {

		// add a user
		UserBean ub1 = new UserBean();
		ub1.setEmailId("a@b.com");
		ub1.setEnabled(true);
		ub1.setFirstName("Test1");
		ub1.setLastName("User1");
		ub1.setMiddleInit("1");
		ub1.setPassword("password");
		ub1.setUsername("ustest1");
		UserBean userBean1 = userService.addUser(ub1);
		Assert.assertNotNull("Failed to create ustest1. Why", userBean1);
		// try to add again
		UserBean userBean2 = userService.addUser(ub1);
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
		Assert.assertTrue("Password change failed",
				"".equalsIgnoreCase(result));
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
		Assert.assertTrue("Password change failed",
				"Old password is invalid.".equalsIgnoreCase(userService
						.changePassword(pcb, myUser)));
		
		// reset password of the user
		userService.resetPassword("ustest1");
		// Is password change needed
		Assert.assertTrue(
				"User is supposed to change password after reset by admin",
				userService.isPasswordChangeNeeded("ustest1"));
		// Update user
		myUser.setEmailId("d@g.com");
		userService.updateUser(myUser);
		// Get user name select items
		Assert.assertNotNull("Expected user select items", userService.getUserNamesSelectItems());
		// Update authorization
		Assert.assertNotNull("Update auth failed", userService.updateAutorization(myUser));
		// delete user
		int result2 = userService.deleteUser(myUser.getId(), "Admin");
		Assert.assertTrue("Failed to delete user", result2 == 0);
	}
}
