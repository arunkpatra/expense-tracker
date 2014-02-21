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

package com.exp.tracker.services.api;

import java.util.List;

import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.ExpenseSearchCriteria;
import com.exp.tracker.data.model.SettlementBean;

/**
 * A service interface for creating and retrieving expenses.
 */
public interface ExpenseService {

	/**
	 * Save an expense.
	 * 
	 * @param expenseDetaild
	 *            Expense detail.
	 * @return int The status of the operation.
	 */
	int saveExpense(ExpenseDetail expenseDetaild);

	/**
	 * Retrieve expenses for a provided expense search criteria.
	 * 
	 * @param expSearchCriteria
	 *            The expense search criteria
	 * @return A list of expenses
	 */
	List<ExpenseDetail> getExpenses(ExpenseSearchCriteria expSearchCriteria);

	/**
	 * Get unsettled expenses for a settlement.
	 * 
	 * @param settlementBean
	 *            The settlement bean
	 * @return A list of expensedetail objects.
	 */
	List<ExpenseDetail> getUnsettledExpenses(SettlementBean settlementBean);

	/**
	 * Get a list of recent expenses.
	 * 
	 * @param lastDays
	 *            The days to consider.
	 * @return A list of expenses.
	 */
	List<ExpenseDetail> getRecentExpenses(int lastDays);

	/**
	 * Get a specific expensedetail record.
	 * 
	 * @param expenseId
	 *            The expense detail Id.
	 * @return The expensedetail record.
	 */
	ExpenseDetail getExpenseById(Long expenseId);

	/**
	 * Get an expense detail bean by ID.
	 * 
	 * @param expenseId
	 *            The expense ID
	 * @return The expense detail record
	 */
	ExpenseDetail getExpenseDetailBeanById(Long expenseId);

	/**
	 * Delete an expense record.
	 * 
	 * @param expenseId
	 *            The expense ID
	 * @return status of the operation.
	 */
	int deleteExpenseById(Long expenseId, RequestContext ctx);

	/**
	 * Returns the expense status.
	 * 
	 * @see com.exp.tracker.data.entities.ExpenseEntity#EXPENSE_STATUS_NEW
	 * @see com.exp.tracker.data.entities.ExpenseEntity#EXPENSE_STATUS_SETTLEMENT_CREATED
	 * @see com.exp.tracker.data.entities.ExpenseEntity#EXPENSE_STATUS_SETTLED
	 * @param expenseId
	 *            The expense Id.
	 * @return
	 */
	String getExpenseStatus(Long expenseId);

}
