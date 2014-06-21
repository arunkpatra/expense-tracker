/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
