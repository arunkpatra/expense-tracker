package com.exp.tracker.services.impl;

import java.util.List;

import javax.faces.model.SelectItem;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.exp.tracker.services.api.RoleService;

public class JpaRoleServiceTests extends AbstractExpenseTrackerBaseTest {

	@Autowired
	private RoleService roleService;

	@Test
	public void roleServiceTests() {
		List<SelectItem> rsi = roleService.getRolesSelectItems();
		Assert.assertNotNull("Failed to get roles", rsi);
		Assert.assertTrue("Expected exactly 3 roles", rsi.size() == 3);
	}
}
