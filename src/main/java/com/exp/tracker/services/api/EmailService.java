package com.exp.tracker.services.api;

import com.exp.tracker.data.model.UserBean;

public interface EmailService {
	public int sendSettlementEmail(Long sid, byte[] settlementReport, byte[] expenseReport);
	
	public void sendWelcomeEmail(UserBean ub);
	
	public void sendPasswordResetEmail(UserBean ub);
}
