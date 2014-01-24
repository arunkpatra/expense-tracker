package com.exp.tracker.services.impl;

import java.util.List;

import javax.faces.model.SelectItem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.exp.tracker.services.api.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/root-applicationContext.xml")
@ActiveProfiles("demo")
@WebAppConfiguration
public class JpaRoleServiceTests {

	@Autowired
	private RoleService roleService;
	
	@Test
	public void roleServiceTests() {
		List<SelectItem> rsi = roleService.getRolesSelectItems();
		Assert.assertNotNull("Failed to get roles",rsi);
		Assert.assertTrue("Expected exactly 3 roles", rsi.size() == 3);
	}
}
