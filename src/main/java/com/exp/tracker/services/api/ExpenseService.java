package com.exp.tracker.services.api;

import java.util.List;

import com.exp.tracker.data.entities.ExpenseEntity;
import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.ExpenseSearchCriteria;
import com.exp.tracker.data.model.SettlementBean;

/**
 * A service interface for creating and retrieving expenses.
 */
public interface ExpenseService
{

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
    List<ExpenseEntity> getExpenses(ExpenseSearchCriteria expSearchCriteria);

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
    int deleteExpenseById(Long expenseId);

}
