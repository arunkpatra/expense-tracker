package com.exp.tracker.utils;

import java.util.List;

import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;

public interface IEmailMessageSender {
	public void sendPasswordResetEmail(UserBean ub);	
	public void sendSettlementNotice(SettlementBean sb, List<UserBean> ul, byte[] settlementReport, byte[] expenseReport);
	public void sendWelcomeEmail(UserBean ub);
}
