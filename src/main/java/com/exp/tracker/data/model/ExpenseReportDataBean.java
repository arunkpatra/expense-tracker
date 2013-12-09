/**
 * Copyright 2013 Arun K Patra

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.exp.tracker.data.model;

import java.io.Serializable;
import java.util.Date;

public class ExpenseReportDataBean implements Serializable{
	private static final long serialVersionUID = 9089973707154328627L;
	
	private long expenseId;
	private Date expenseDate;
	private float expenseAmount;
	private String expenseDescription;
	private String paidBy;
	private float userShareAmount;
	private String userName;
	public long getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(long expenseId) {
		this.expenseId = expenseId;
	}
	public Date getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}
	public float getExpenseAmount() {
		return expenseAmount;
	}
	public void setExpenseAmount(float expenseAmount) {
		this.expenseAmount = expenseAmount;
	}
	public String getExpenseDescription() {
		return expenseDescription;
	}
	public void setExpenseDescription(String expenseDescription) {
		this.expenseDescription = expenseDescription;
	}
	public String getPaidBy() {
		return paidBy;
	}
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	public float getUserShareAmount() {
		return userShareAmount;
	}
	public void setUserShareAmount(float userShareAmount) {
		this.userShareAmount = userShareAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
