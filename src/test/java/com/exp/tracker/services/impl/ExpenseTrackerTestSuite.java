package com.exp.tracker.services.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ JpaAuthServiceTests.class, JpaExpenseServiceTests.class,
		JpaRoleServiceTests.class, JpaSettlementServiceTests.class,
		JpaUserServiceTests.class, JasperReportGenerationServiceTests.class })
public class ExpenseTrackerTestSuite {

}
