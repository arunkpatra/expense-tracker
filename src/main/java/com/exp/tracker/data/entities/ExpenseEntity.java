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
package com.exp.tracker.data.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "et_expense")
@NamedQueries({
        @NamedQuery(name = "unsettledExpenseForUser", query = "SELECT SUM(u.shareAmount) FROM ExpenseEntity e, UserExpenseEntity u "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) AND "
                + "(e.id = u.expense_id) AND "
                + "(e.settlementId = NULL) AND " + "(u.username = :userName)"),
        @NamedQuery(name = "unsettledAmountPaidByUser", query = "SELECT SUM(amount) FROM ExpenseEntity e "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) AND "
                + "(e.settlementId = NULL) AND "
                + "(e.paidBy = :paidBy)"),
        @NamedQuery(name = "expenseForUser", query = "SELECT SUM(u.shareAmount) FROM ExpenseEntity e, UserExpenseEntity u "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) AND "
                + "(e.id = u.expense_id) AND "
                + "(u.username = :userName)"),
        @NamedQuery(name = "anyExpensesForUser", query = "SELECT e FROM ExpenseEntity e, UserExpenseEntity u "
                + "WHERE "
                + "(e.id = u.expense_id) AND "
                + "((u.username = :userName) OR (e.paidBy = :userName))"),
        @NamedQuery(name = "amountPaidByUser", query = "SELECT SUM(amount) FROM ExpenseEntity e "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) AND " + "e.paidBy = :paidBy"),
        @NamedQuery(name = "getExpenses", query = "SELECT e FROM ExpenseEntity e "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) ORDER BY e.date DESC"),
        @NamedQuery(name = "getUnsettledExpenses", query = "SELECT e FROM ExpenseEntity e "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) AND (e.settlementId = NULL) ORDER BY e.date DESC"),
        @NamedQuery(name = "addSettlementId", query = "UPDATE ExpenseEntity e SET e.settlementId = :settlementId "
                + "WHERE (e.date >= :startDate) AND "
                + "(e.date <= :endDate) AND " + "e.settlementId = NULL"),
        @NamedQuery(name = "getExpenseById", query = "SELECT e FROM ExpenseEntity e "
                + "WHERE e.id = :id"),
        @NamedQuery(name = "deleteExpensesForSid", query = "DELETE FROM ExpenseEntity e "
                + "WHERE e.settlementId = :sid"),
        @NamedQuery(name = "getExpensesForUser", query = "SELECT e FROM ExpenseEntity e "
                + "WHERE (e.paidBy = :paidBy) AND "
                + "(e.date >= :startDate) AND " + "(e.date <= :endDate)") })
public class ExpenseEntity implements Serializable
{

    private static final long serialVersionUID = -3326698472797161467L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String description;

    private String category;

    private String paidBy;

    @Column(name = "createdby")
    private String createdBy;

    @Column(name = "settlement_id")
    private Long settlementId;

    /**
     * There may be many expenses for a settlemnt
     */
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "settlement_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SettlementEntity settlement;

    @OneToMany(targetEntity = UserExpenseEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "expense")
    private Set<UserExpenseEntity> userExpenseSet;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public ExpenseEntity() {
    }

    public Set<UserExpenseEntity> getUserExpenseSet()
    {
        return userExpenseSet;
    }

    public void setUserExpenseSet(Set<UserExpenseEntity> userExpenseSet)
    {
        this.userExpenseSet = userExpenseSet;
    }

    public String getPaidBy()
    {
        return paidBy;
    }

    public void setPaidBy(String paidBy)
    {
        this.paidBy = paidBy;
    }

    public Long getSettlementId()
    {
        return settlementId;
    }

    public void setSettlementId(Long settlementId)
    {
        this.settlementId = settlementId;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public SettlementEntity getSettlement()
    {
        return settlement;
    }

    public void setSettlement(SettlementEntity settlement)
    {
        this.settlement = settlement;
    }

}
