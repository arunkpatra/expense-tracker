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

package com.exp.tracker.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.entities.UserSettlementEntity;
import com.exp.tracker.data.model.PaymentBean;
import com.exp.tracker.services.api.PaymentService;
@Service("paymentService")
@Repository
public class JpaPaymentService implements PaymentService {
	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	@SuppressWarnings("unchecked")
	public List<PaymentBean> getPaymnentsForUser(String userName) {
		List<PaymentBean> paymentList = new ArrayList<PaymentBean>();
		Query queryGetPaymentsForUser = null;
		queryGetPaymentsForUser = em.createNamedQuery("paymentsForUser");
		queryGetPaymentsForUser.setParameter("userName", userName);
		// get a list of results
		Collection<Object[]> results = queryGetPaymentsForUser.getResultList();
		for (Object[] oa : results) {
			PaymentBean pb = new PaymentBean();
			// Each object is a list
			pb.setAmount(((Float)oa[0]).floatValue());
			pb.setSettledFlag(((Integer)oa[1]).intValue());
			pb.setStartDate(((Date)oa[2]));
			pb.setEndDate(((Date)oa[3]));
			pb.setSettlementId(((Long)oa[4]).longValue());	
			pb.setUserName(((String)oa[5]));	
			paymentList.add(pb);
		}
		return paymentList;

	}
	@SuppressWarnings("unchecked")
	public List<PaymentBean> getAllPayments() {
		List<PaymentBean> paymentList = new ArrayList<PaymentBean>();
		Query queryGetAllPayments = null;
		queryGetAllPayments = em.createNamedQuery("allPayments");
		// get a list of results
		Collection<Object[]> results = queryGetAllPayments.getResultList();
		for (Object[] oa : results) {
			PaymentBean pb = new PaymentBean();
			// Each object is a list
			pb.setAmount(((Float)oa[0]).floatValue());
			pb.setSettledFlag(((Integer)oa[1]).intValue());
			pb.setStartDate(((Date)oa[2]));
			pb.setEndDate(((Date)oa[3]));
			pb.setSettlementId(((Long)oa[4]).longValue());	
			pb.setUserName(((String)oa[5]));
			pb.setUserSettlementId(((Long)oa[6]).longValue());
			paymentList.add(pb);
		}
		return paymentList;
	}
	@Transactional
	public void applyUserPayment(Long usId, RequestContext ctx) {
		UserSettlementEntity use = new UserSettlementEntity();
		use = em.find(UserSettlementEntity.class, usId);
		use.setSettlementFlag(UserSettlementEntity.SETTLEMENT_COMPLETED);
		Date today = new Date();
		use.setSettledDate(today);
		em.merge(use);	
		ctx.getMessageContext().addMessage(
				new MessageBuilder().info()
						.defaultText("Transaction processed successfuly.").build());
	}

}
