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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.entities.AuthEntity;
import com.exp.tracker.data.entities.ExpenseEntity;
import com.exp.tracker.data.entities.RoleEntity;
import com.exp.tracker.data.entities.SettlementEntity;
import com.exp.tracker.data.entities.UserEntity;
import com.exp.tracker.data.entities.UserExpenseEntity;
import com.exp.tracker.data.model.ExpenseDetail;
import com.exp.tracker.data.model.ExpenseSearchCriteria;
import com.exp.tracker.data.model.UserShare;
import com.exp.tracker.services.api.ExpenseService;
import com.exp.tracker.data.model.SettlementBean;

@Service("expenseService")
@Repository
public class JpaExpenseService implements ExpenseService {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Transactional
	public int saveExpense(ExpenseDetail ed) {
		int result = 0;
		ExpenseEntity ex;
		if (null == ed.getSettlementId()) {
			// calculate shares if not calculated yet.
			if (!ed.isOverrideSharesFlag()) {
				ed.calculateShareAmounts();
			}
			// we need to merge if the user is editing this record
			if (ed.isEditMode()) {
				ex = em.find(ExpenseEntity.class, ed.getId());
				// now put back field by field
				ex.setAmount(ed.getAmount());
				ex.setCategory(ed.getCategory());
				ex.setCreatedBy(ed.getCreatedBy());
				ex.setDate(ed.getDate());
				ex.setDescription(ed.getDescription());
				ex.setPaidBy(ed.getPaidBy());
				ex.setSettlementId(ed.getSettlementId());
				// you can not do any of these below. JPA does not support
				// ex.setUserExpenseSet(null);
				// ex.setUserExpenseSet(new ArrayList<UserExpenseEntity>());
				// first delete all children
				Query deleteUserExpenseEntitiesForExpense = em
						.createNamedQuery("deleteUserExpenseEntitiesForExpense");
				deleteUserExpenseEntitiesForExpense.setParameter("expense_id",
						ex.getId());
				deleteUserExpenseEntitiesForExpense.executeUpdate();
				// children deleted
				ex.setUserExpenseSet(null);
				em.merge(ex);
			} else {
				ex = ed.getExpenseEntity();
				em.persist(ex);
				// em.flush();
			}
			// now merge children
			Long id = ex.getId();
			Set<UserExpenseEntity> userExpenseSet = new HashSet<UserExpenseEntity>();
			for (UserShare us : ed.getUserShares()) {
				if (us.isParticipationFlag()) {
					UserExpenseEntity uee = new UserExpenseEntity();
					uee.setUsername(us.getName());
					uee.setExpense_id(id);
					uee.setShareAmount(us.getShareAmount());
					uee.setDiscountPercent(us.getDiscountPercent());
					userExpenseSet.add(uee);
				}
			}
			ex.setUserExpenseSet(userExpenseSet);
			em.merge(ex);
			em.flush();

		} else {
			// you cant edit a settled record
			result = 1;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ExpenseDetail> getExpenses(ExpenseSearchCriteria esc) {

		Query queryGetExpenses = null;
		queryGetExpenses = em.createNamedQuery("getExpenses");
		queryGetExpenses.setParameter("startDate", esc.getStartDate());
		queryGetExpenses.setParameter("endDate", esc.getEndDate());
		List<ExpenseEntity> eeList = queryGetExpenses.getResultList();
		List<ExpenseDetail> edList = new ArrayList<ExpenseDetail>();
		for (ExpenseEntity ee : eeList) {
		    edList.add(new ExpenseDetail(ee));
		}
		return edList;

	}

	@Transactional(readOnly = true)
	public ExpenseDetail getExpenseById(Long id) {
		ExpenseEntity returnExpenseEntity = null;
		Query queryGetExpenseById = em.createNamedQuery("getExpenseById");
		queryGetExpenseById.setParameter("id", id);
		ExpenseEntity ee = (ExpenseEntity) queryGetExpenseById
				.getSingleResult();
		if (null != ee.getSettlementId()) {
			// this means record has been settled.
			Query queryGetSettlementForId = em
					.createNamedQuery("getSettlementForId");
			queryGetSettlementForId.setParameter("id", ee.getSettlementId());
			SettlementEntity se = (SettlementEntity) queryGetSettlementForId
					.getSingleResult();
			for (ExpenseEntity ee1 : se.getExpenseSet()) {
				if (id.longValue() == ee1.getId().longValue()) {
					returnExpenseEntity = ee1;
					break;
				}
			}
		} else {
			returnExpenseEntity = ee;
		}
		return new ExpenseDetail(returnExpenseEntity);
	}

	@Transactional
	public int deleteExpenseById(Long expenseId, RequestContext ctx) {
		int result = 0;
		ExpenseEntity ee = em.find(ExpenseEntity.class, expenseId);
		if (null == ee.getSettlementId()) {
			em.remove(ee);
			ctx.getMessageContext().addMessage(
                    new MessageBuilder().info()
                            .defaultText("Expense deleted successfuly.").build());
		} else {
			// expense settled already. cant delete
		    ctx.getMessageContext().addMessage(
                    new MessageBuilder().error()
                            .defaultText("Expense could not be deleted.").build());
			result = 1;
		}
		return result;
	}

	@Override
	@Transactional
	public void deleteSelectedExpenses(List<ExpenseDetail> expenses,
			RequestContext ctx) {
		int i = 0;
		for (ExpenseDetail ed : expenses) {
			if (ed.isDeleteExpenseFlag()) {
				if (null != ed.getId()) {
					ExpenseEntity ee = em.find(ExpenseEntity.class, ed.getId());
					em.remove(ee);
					i +=1;
				}			
			}
		}
		if (i ==0) {
			ctx.getMessageContext().addMessage(
                    new MessageBuilder().error()
                            .defaultText("No expenses were selected to be deleted.").build());
		} else {
			ctx.getMessageContext().addMessage(
                    new MessageBuilder().error()
                            .defaultText("Selected expenses were deleted succesfuly.").build());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public ExpenseDetail getExpenseDetailBeanById(Long id) {
		ExpenseDetail ed;
		boolean newExpense = false;
		if (null == id) {
			ed = new ExpenseDetail();
			SecurityContext ctx = SecurityContextHolder.getContext();
			String userName = ctx.getAuthentication().getName();
			boolean thisIsAUser = false;
			for (GrantedAuthority ga : ctx.getAuthentication().getAuthorities()) {
				if (ga.getAuthority().equalsIgnoreCase(RoleEntity.ROLE_USER)) {
					thisIsAUser = true;
				}
			}
			if (thisIsAUser) {
				ed.setPaidBy(userName);
			}
			newExpense = true;

		} else {
			ExpenseEntity ee = em.find(ExpenseEntity.class, id);
			ed = new ExpenseDetail(ee);
			// now add those non participating users as well.
		}
		// now set some users here...
		// now form a map of participating users
		Map<String, UserShare> currentUserMap = new HashMap<String, UserShare>();
		for (UserShare us : ed.getUserShares()) {
			currentUserMap.put(us.getName(), us);
		}
		// get all allowed users
		Query queryGetAllUsers = em.createNamedQuery("getAllUsers");
		Collection<UserEntity> users = queryGetAllUsers.getResultList();
		List<UserEntity> userEntitySet = new ArrayList<UserEntity>(users);
		// iterate thru all users in the system
		for (UserEntity ue : userEntitySet) {
			// only consider enabled users
			if ((ue.getEnabled() != UserEntity.USER_DISABLED) && isUserRole(ue)) {
				// make a new entry only if the share does not exist
				if (!currentUserMap.containsKey(ue.getUsername())) {
					UserShare us = new UserShare(ue.getUsername(), 0.00f,
							0.00f, true);
					if (!newExpense) {
						us.setParticipationFlag(false);
					}
					currentUserMap.put(us.getName(), us);
				}
			}
		}
		// now set the shares
		List<UserShare> userSharesList = new ArrayList<UserShare>();
		Set<String> keySet = currentUserMap.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			userSharesList.add((UserShare) currentUserMap.get(key));
		}
		// set users here
		ed.setUserShares(userSharesList);
		// return object
		return ed;
	}

	private boolean isUserRole(UserEntity ue) {
		boolean result = false;
		for (AuthEntity ae : ue.getAuthSet()) {
			if (ae.getAuthority().equalsIgnoreCase(RoleEntity.ROLE_USER)) {
				result = true;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ExpenseDetail> getUnsettledExpenses(SettlementBean sb) {
		Query queryGetUnsettledExpenses = null;
		queryGetUnsettledExpenses = em.createNamedQuery("getUnsettledExpenses");
		queryGetUnsettledExpenses.setParameter("startDate", sb.getStartDate());
		queryGetUnsettledExpenses.setParameter("endDate", sb.getEndDate());
		List<ExpenseEntity> expensesList = queryGetUnsettledExpenses
				.getResultList();
		List<ExpenseDetail> edList = new ArrayList<ExpenseDetail>();
		for (ExpenseEntity ee : expensesList) {
			edList.add(new ExpenseDetail(ee));
		}
		return edList;
	}

	public List<ExpenseDetail> getRecentExpenses(int lastDays) {
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public String getExpenseStatus(Long expenseId) {
		ExpenseDetail ed = getExpenseById(expenseId);
		if (null == ed.getSettlementId()) {
			return ExpenseEntity.EXPENSE_STATUS_NEW;
		} else {
			Query queryGetSettlementStatus = null;
			queryGetSettlementStatus = em
					.createNamedQuery("getSettlementStatus");
			queryGetSettlementStatus.setParameter("sid", ed.getSettlementId());
			Integer intResult = (Integer) queryGetSettlementStatus
					.getSingleResult();
			int r = intResult.intValue();
			if (r == 0) {
				return ExpenseEntity.EXPENSE_STATUS_SETTLEMENT_CREATED;
			} else {
				if (r == 1) {
					return ExpenseEntity.EXPENSE_STATUS_SETTLED;
				} else {
					return "Unknown";
				}
			}
		}
	}

	

}
