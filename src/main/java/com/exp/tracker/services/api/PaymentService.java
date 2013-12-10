package com.exp.tracker.services.api;

import java.util.List;

import com.exp.tracker.data.model.PaymentBean;

public interface PaymentService {

	public List<PaymentBean> getPaymnentsForUser(String userName);
	
	public List<PaymentBean> getAllPayments();
	
	public void applyUserPayment(Long usId);
}
