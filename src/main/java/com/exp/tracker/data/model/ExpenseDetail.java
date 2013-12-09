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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

import com.exp.tracker.data.entities.ExpenseEntity;
import com.exp.tracker.data.entities.UserExpenseEntity;

/**
 * A backing bean for the expense detail entry screen.
 */
public class ExpenseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public ExpenseDetail() {
    	Calendar calendar = Calendar.getInstance();
    	setDate(calendar.getTime());
    	setUserShares(new ArrayList<UserShare>());
	}
    
    @SuppressWarnings("unchecked")
	public ExpenseDetail(ExpenseEntity ee) {
    	setId(ee.getId());
    	setDate(ee.getDate());
    	setPaidBy(ee.getPaidBy());
    	setCreatedBy(ee.getCreatedBy());
    	setAmount(ee.getAmount());
    	setDescription(ee.getDescription());
    	setCategory(ee.getCategory());
    	setSettlementId(ee.getSettlementId());
    	// critical flag
    	setEditMode(true);
    	//
    	userShares = new ArrayList();
    	for (UserExpenseEntity uee : ee.getUserExpenseSet()) {
    		UserShare us = new UserShare(uee);
    		userShares.add(us);
    	}
    }

    private Long id;
    private Date date;    
    private String paidBy;    
    private String createdBy;    
	private float amount;    
    private String description = "";    
    private String category = "";    
    private Long settlementId;    
    private boolean overrideSharesFlag = false;    
    private boolean editMode;
    
    private List<UserShare> userShares;
    
    public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public ExpenseEntity getExpenseEntity() {
	return new ExpenseEntity(this);	
	}

	public List<UserShare> getUserShares() {
		return userShares;
	}

	public void setUserShares(List<UserShare> userShares) {
		this.userShares = userShares;
	}

	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	
//	public void setUserShareEntities(Set<UserEntity> ues) {
//		List<UserShare> userSharesList = new ArrayList<UserShare>();
//		for (UserEntity ue : ues) {
//			if (ue.getEnabled() != UserEntity.USER_DISABLED) {
//				UserShare us = new UserShare(ue.getUsername(),0.00f, 0.00f,true);
//				userSharesList.add(us);	
//			}
//		}
//		this.setUserShares(userSharesList);		
//	}
	
	/**
	 * Calculates individual share amounts.
	 * 
	 * This method assumes that the net amount for the expense is set. 
	 * 
	 */
	public void calculateShareAmounts() {
		List<UserShare> usList = getUserShares();
		int participants = 0;
		for (UserShare us : usList) { 
			if (us.isParticipationFlag()) {
				participants = participants + 1;
			}
		}
		for (UserShare us : usList) {
			// only those who participate
			if (us.isParticipationFlag()) {
				us.setShareAmount(getAmount()/participants);
			} else
			{
				us.setShareAmount(0.00f);
			}
			
		}		
	}
	/**
	 * User wants to override share amounts
	 */
	public void overrideShareAmounts() {
		setOverrideSharesFlag(true);
	}
	
	public void editRecord() {
		setEditMode(true);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void validateEnterExpenseDetail(ValidationContext context) {
		String userEventName = context.getUserEvent();
		// this is a hack!
		if (!isEditMode()) {
			setCreatedBy(context.getUserPrincipal().getName());
		}
		// handle specific events
		if ("calcShares".equalsIgnoreCase(userEventName)) {
			setOverrideSharesFlag(false);
		}
		if (isOverrideSharesFlag()) {
			float sumOfShares = 0f;
			for (UserShare us : getUserShares()) {
				if (us.isParticipationFlag()) {
					sumOfShares = sumOfShares + us.getShareAmount();
				}
			}
			if (sumOfShares != getAmount()) {
				MessageContext messages = context.getMessageContext();	
				messages.addMessage(new MessageBuilder().error().code("total.amount.incorrect").build());
			}
		} else {
			calculateShareAmounts();
		}
		if ("next".equalsIgnoreCase(userEventName)) {
			if (getAmount() == 0f) 
			{
				MessageContext messages = context.getMessageContext();	
				messages.addMessage(new MessageBuilder().error().code("zero.amount.disallowed").build());
			}
			int participants = 0;
			for (UserShare us : getUserShares()) { 
				if (us.isParticipationFlag()) {
					participants = participants + 1;
				}
			}
			if(participants == 0) {
				MessageContext messages = context.getMessageContext();	
				messages.addMessage(new MessageBuilder().error().code("no.participant.chosen").build());
			}
		}
	}

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public boolean isOverrideSharesFlag() {
		return overrideSharesFlag;
	}

	public void setOverrideSharesFlag(boolean overrideSharesFlag) {
		this.overrideSharesFlag = overrideSharesFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
}