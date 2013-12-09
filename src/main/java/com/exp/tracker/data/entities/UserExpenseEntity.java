package com.exp.tracker.data.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="et_user_expense")
@NamedQueries( {
		@NamedQuery(name = "deleteUserExpenseEntitiesForExpense", query = "DELETE UserExpenseEntity uee " +
				"WHERE uee.expense_id = :expense_id")})
public class UserExpenseEntity implements Serializable{
	
	private static final long serialVersionUID = -1980648185432872922L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	
	private String username;
	
	@Column(name="amount")
	private float shareAmount;	
	
	@Column(name="discountpercent")
	private float discountPercent;	
	
	private Long expense_id;
	
	@ManyToOne (cascade=CascadeType.REMOVE)
	@JoinColumn(name="expense_id", referencedColumnName="id", insertable=false, updatable=false)
	private ExpenseEntity expense;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ExpenseEntity getExpense() {
		return expense;
	}

	public void setExpense(ExpenseEntity expense) {
		this.expense = expense;
	}

	public Long getExpense_id() {
		return expense_id;
		//return getExpense().getId();
	}

	public void setExpense_id(Long expenseId) {
		expense_id = expenseId;
	}

	public float getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(float shareAmount) {
		this.shareAmount = shareAmount;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

}
