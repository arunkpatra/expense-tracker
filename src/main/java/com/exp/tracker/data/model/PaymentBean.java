package com.exp.tracker.data.model;

import java.io.Serializable;
import java.util.Date;

public class PaymentBean implements Serializable{
	private static final long serialVersionUID = 6802440623608016400L;
	private float amount;
	private Date startDate;
	private Date endDate;
	private int settledFlag;
	private Long settlementId;
	private Long userSettlementId;
	private String userName;
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
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
	public int getSettledFlag() {
		return settledFlag;
	}
	public void setSettledFlag(int settledFlag) {
		this.settledFlag = settledFlag;
	}
	public Long getSettlementId() {
		return settlementId;
	}
	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserSettlementId() {
		return userSettlementId;
	}
	public void setUserSettlementId(Long userSettlementId) {
		this.userSettlementId = userSettlementId;
	}
	
}
