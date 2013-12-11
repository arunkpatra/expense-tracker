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
import java.util.Date;
import java.util.List;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

import com.exp.tracker.data.entities.SettlementEntity;
import com.exp.tracker.data.entities.UserSettlementEntity;

public class SettlementBean implements Serializable {

	private static final long serialVersionUID = 9199992347686275656L;
	
	private Long id;
	private Date startDate;
	private Date endDate;
	private Date createdDate;
	private Date closedDate;
	private int settlementFlag;	
	private float volume;
	private String accountManager;
	
//	private byte[] reportPdf;
//	private byte[] expenseReportPdf;
	private List<UserSettlementBean> userSettlementList;

	public SettlementBean() {
		
	}
	
	public SettlementBean(SettlementEntity se) {
		setId(se.getId());
		setEndDate(se.getEndDate());
		setStartDate(se.getStartDate());
		setSettlementFlag(se.getSettlementCompleted());
		setCreatedDate(se.getCreatedDate());
		setClosedDate(se.getClosedDate());
		setVolume(se.getVolume());
//		setReportPdf(se.getReportPdf());
//		setExpenseReportPdf(se.getExpenseReportPdf());
		setAccountManager(se.getAccountManager());
		List<UserSettlementBean> usl = new ArrayList<UserSettlementBean>(); 
		for (UserSettlementEntity use : se.getUserSettlementSet()) {
			usl.add(new UserSettlementBean(use));
		}
		setUserSettlementList(usl);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<UserSettlementBean> getUserSettlementList() {
		return userSettlementList;
	}
	public void setUserSettlementList(List<UserSettlementBean> userSettlementList) {
		this.userSettlementList = userSettlementList;
	}
	public int getSettlementFlag() {
		return settlementFlag;
	}
	public void setSettlementFlag(int settlementFlag) {
		this.settlementFlag = settlementFlag;
	}
	
	public void validateSettlementGenerationEntry(ValidationContext context) {
		String userEventName = context.getUserEvent();
		if ((userEventName.equalsIgnoreCase("completeSettlement")) ||
				(userEventName.equalsIgnoreCase("delete"))) {
			// nothing
		} else {
			if (getStartDate().after(getEndDate())) {
				MessageContext messages = context.getMessageContext();	
				messages.addMessage(new MessageBuilder().error().code("startdate.greaterthan.enddate").build());
			}
		}
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

//	public byte[] getReportPdf() {
//		return reportPdf;
//	}
//
//	public void setReportPdf(byte[] reportPdf) {
//		this.reportPdf = reportPdf;
//	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

//	public byte[] getExpenseReportPdf() {
//		return expenseReportPdf;
//	}
//
//	public void setExpenseReportPdf(byte[] expenseReportPdf) {
//		this.expenseReportPdf = expenseReportPdf;
//	}
}
