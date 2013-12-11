package com.exp.tracker.services.api;

import java.util.List;

import com.exp.tracker.data.entities.ExpenseEntity;
import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.ExpenseSearchCriteria;
import com.exp.tracker.data.model.SettlementBean;


/**
 * A service interface for creating and retrieving expenses
 */
public interface ExpenseService {

    /**
     * Create a new, transient hotel booking instance for the given user.
     * @param hotelId the hotelId
     * @param userName the user name
     * @return the new transient booking instance
     */
    public int saveExpense(ExpenseDetail ed);
    
    public List<ExpenseEntity> getExpenses(ExpenseSearchCriteria esc);
    
    public List<ExpenseDetail> getUnsettledExpenses(SettlementBean sb);
    
    public List<ExpenseDetail> getRecentExpenses(int lastDays);
    
    public ExpenseDetail getExpenseById(Long id);
    
    public ExpenseDetail getExpenseDetailBeanById(Long id);
    
    public int deleteExpenseById(Long expenseId);

}
