package com.exp.tracker.data.model;

import java.io.Serializable;

import com.exp.tracker.data.entities.UserSettlementEntity;

public class UserSettlementBean implements Serializable{
	
	private static final long serialVersionUID = -3200103492516523232L;
	private Long id;
	/**
	 * This is the total amount payable/receivable for this user
	 */
	private float amount;
	private String userName;
	private int settledFlag;
	/**
	 * The share amount for this user
	 */
	private float userShare;
	/**
	 * The amount the user has paid for this settlement already
	 */
	private float userPaid;
	
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSettledFlag() {
		return settledFlag;
	}
	public void setSettledFlag(int settledFlag) {
		this.settledFlag = settledFlag;
	}
	
	public UserSettlementBean() {
		
	}
	
	public UserSettlementBean(UserSettlementEntity use) {
		setId(use.getId());
		setSettledFlag(use.getSettlementFlag());
		setAmount(use.getAmount());
		setUserShare(use.getUserShare());
		setUserPaid(use.getUserPaid());
		setUserName(use.getUserName());
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public float getUserShare() {
		return userShare;
	}
	public void setUserShare(float userShare) {
		this.userShare = userShare;
	}
	public float getUserPaid() {
		return userPaid;
	}
	public void setUserPaid(float userPaid) {
		this.userPaid = userPaid;
	}

}
