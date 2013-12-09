package com.exp.tracker.data.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

public class ExpenseSearchCriteria implements Serializable{

	private static final long serialVersionUID = 6011526801560855519L;
	
	public static final String PAIDBY_ALL = "All";

	private Date startDate;
	
	private Date endDate;
	
//	private String paidBy = PAIDBY_ALL;
	private String paidBy;

	private String sortBy = "date";
	
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

	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	
	public void validateEnterExpenseSearchCriteria(ValidationContext context) {
		if (getStartDate().after(getEndDate())) {
			MessageContext messages = context.getMessageContext();	
			messages.addMessage(new MessageBuilder().error().code("startdate.lessthan.enddate").build());
		}
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
}
