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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ JpaAuthServiceTests.class, JpaExpenseServiceTests.class,
		JpaRoleServiceTests.class, JpaSettlementServiceTests.class,
		JpaUserServiceTests.class, JasperReportGenerationServiceTests.class,
		ReportControllerTests.class, GroupServiceImplTests.class,
		TestPasswordgeneratorTests.class })
public class ExpenseTrackerTestSuite {

}
