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
package com.exp.tracker.data.model;

import java.io.Serializable;

import com.exp.tracker.data.entities.UserExpenseEntity;

public class UserShare implements Serializable {

	public UserShare(UserExpenseEntity uee) {
		setId(uee.getId());
		setExpense_id(uee.getExpense_id());
		setName(uee.getUsername());
		setDiscountPercent(uee.getDiscountPercent());
		setShareAmount(uee.getShareAmount());
		// the fact that we have an UEE here means that the user is a participant
		setParticipationFlag(true);		
	}
	public UserShare(String name, float shareAmount, float discountPercent, boolean isParticipant) {
		this.name = name;
		this.shareAmount = shareAmount;
		this.discountPercent = discountPercent;
		this.participationFlag = isParticipant;
	}
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long expense_id;
	private float shareAmount;
	private float discountPercent;	
	private boolean participationFlag;
	private String name;
	public boolean isParticipationFlag() {
		return participationFlag;
	}
	public void setParticipationFlag(boolean participationFlag) {
		this.participationFlag = participationFlag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getExpense_id() {
		return expense_id;
	}
	public void setExpense_id(Long expenseId) {
		expense_id = expenseId;
	}
}
