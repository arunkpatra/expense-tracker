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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.RequestContext;

import com.exp.tracker.data.entities.AuthEntity;
import com.exp.tracker.data.entities.ExpenseEntity;
import com.exp.tracker.data.entities.RoleEntity;
import com.exp.tracker.data.entities.SettlementEntity;
import com.exp.tracker.data.entities.UserEntity;
import com.exp.tracker.data.entities.UserSettlementEntity;
import com.exp.tracker.data.model.ExpenseReportDataBean;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.services.api.SettlementService;

@Service("settlementService")
@Repository
public class JpaSettlementService implements SettlementService
{

    /**
     * The logger.
     */
    private static final Log logger = LogFactory
            .getLog(JpaSettlementService.class);

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em)
    {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Long createSettlement(SettlementBean sb, RequestContext ctx)
    {
        Long result = 0l;
        // create new settlement
        SettlementEntity se = new SettlementEntity();
        Query queryGetExpenses = null;
        queryGetExpenses = em.createNamedQuery("getUnsettledExpenses");
        queryGetExpenses.setParameter("startDate", sb.getStartDate());
        queryGetExpenses.setParameter("endDate", sb.getEndDate());
        Collection<ExpenseEntity> expenses = queryGetExpenses.getResultList();
        if (expenses.size() == 0) {
        	ctx.getMessageContext().
        		addMessage(new MessageBuilder().warning().defaultText("No expenses to settle.").build());
            return 0l; // get out now
        }
        
        // calculate volume
        float volume = 0.0f;
        for (ExpenseEntity ee : expenses) {
            volume = volume + ee.getAmount();
        }
        // set volume
        se.setVolume(volume);
        se.setStartDate(sb.getStartDate());
        se.setEndDate(sb.getEndDate());
        Calendar cal = Calendar.getInstance();
        se.setCreatedDate(cal.getTime());
        se.setAccountManager(sb.getAccountManager());
        se.setSettlementCompleted(SettlementEntity.SETTLEMENT_NOT_COMPLETED);
        em.persist(se); // settlement created at root level
        // an indicator that we succeded
        result = se.getId();

        // get all users now
        Query queryGetUsers = em.createNamedQuery("getAllUsers");
        Query queryExpenseForUser = em
                .createNamedQuery("unsettledExpenseForUser");
        Query queryAmountPaidByUser = em
                .createNamedQuery("unsettledAmountPaidByUser");
        Collection<UserEntity> users = queryGetUsers.getResultList(); // user
                                                                      // list
                                                                      // obtained
        Set<UserSettlementEntity> usl = new HashSet<UserSettlementEntity>();
        for (UserEntity u : users) {
            boolean thisIsAnUser = false;
            for (AuthEntity ae : u.getAuthSet()) {
                if (ae.getAuthority().equalsIgnoreCase(RoleEntity.ROLE_USER)) {
                    thisIsAnUser = true;
                    break;
                }
            }

            UserSettlementEntity use = new UserSettlementEntity();
            use.setSettlement_id(se.getId());
            use.setSettlementFlag(UserSettlementEntity.SETTLEMENT_NOT_COMPLETED);
            // obtain expenses for user
            queryExpenseForUser.setParameter("startDate", sb.getStartDate());
            queryExpenseForUser.setParameter("endDate", sb.getEndDate());
            queryExpenseForUser.setParameter("userName", u.getUsername());
            Double expenseForUser = (Double) queryExpenseForUser
                    .getSingleResult();
            if (null == expenseForUser) {
                expenseForUser = 0.0d;
            }
            // total share for this user
            use.setUserShare(Float.parseFloat(Double.toString(expenseForUser)));

            // obtain total paid by user
            queryAmountPaidByUser.setParameter("startDate", sb.getStartDate());
            queryAmountPaidByUser.setParameter("endDate", sb.getEndDate());
            queryAmountPaidByUser.setParameter("paidBy", u.getUsername());
            Double paidByUser = (Double) queryAmountPaidByUser
                    .getSingleResult();

            if (null == paidByUser) {
                paidByUser = 0.0d;
            }
            // total paid by this user
            use.setUserPaid(Float.parseFloat(Double.toString(paidByUser)));
            //
            // set net amount - payable or receivable
            use.setAmount(Float.parseFloat(Double.toString(paidByUser))
                    - Float.parseFloat(Double.toString(expenseForUser)));
            if (use.getAmount() == 0.0f) {
                // auto settled
                use.setSettlementFlag(UserSettlementEntity.SETTLEMENT_COMPLETED);
                use.setSettledDate(new Date());
            }
            use.setUserName(u.getUsername());
            if (thisIsAnUser || (expenseForUser > 0.0d)) {
                usl.add(use);
            }
        }
        se.setUserSettlementSet(usl);
        em.merge(se); // individual amounts merged

        // now update expenses
        Query queryAddSettlementId = null;
        queryAddSettlementId = em.createNamedQuery("addSettlementId");
        queryAddSettlementId.setParameter("startDate", sb.getStartDate());
        queryAddSettlementId.setParameter("endDate", sb.getEndDate());
        queryAddSettlementId.setParameter("settlementId", se.getId());
        queryAddSettlementId.executeUpdate(); // expenses updated
        ctx.getMessageContext().
		addMessage(new MessageBuilder().info().defaultText("Settlement was created succesfuly.").build());
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<SettlementBean> getSettlements()
    {
        Query queryGetAllSettlements = null;
        queryGetAllSettlements = em.createNamedQuery("getAllSettlements");
        Collection<SettlementEntity> settlements = queryGetAllSettlements
                .getResultList();
        List<SettlementBean> sbl = new ArrayList<SettlementBean>();

        for (SettlementEntity se : settlements) {
            SettlementBean sb = new SettlementBean(se);
            sbl.add(sb);
        }
        return sbl;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public int completeSettlement(Long sid)
    {
        int result = 0;
        Query queryGetUserSettlementsForSettlementId = em
                .createNamedQuery("getUnsettledUserSettlementsForSettlementId");
        queryGetUserSettlementsForSettlementId
                .setParameter("settlementId", sid);
        Collection<UserSettlementEntity> unsettledUseList = queryGetUserSettlementsForSettlementId
                .getResultList();
        if (unsettledUseList.size() == 0) {
            SettlementEntity se = new SettlementEntity();
            se = em.find(SettlementEntity.class, sid);
            se.setSettlementCompleted(SettlementEntity.SETTLEMENT_COMPLETED);
            Calendar cal = Calendar.getInstance();
            se.setClosedDate(cal.getTime());
            em.merge(se);
        } else {
            // You can't close a settlement if there are still records to
            // settle!
            result = 1;
        }
        //
        return result;
    }

    public SettlementBean getSettlementById(Long id)
    {
        SettlementEntity se = em.find(SettlementEntity.class, id);
        SettlementBean sb = new SettlementBean(se);
        return sb;
    }

    @SuppressWarnings("unchecked")
    public List<ExpenseReportDataBean> getExpensesForSettlementId(Long id)
    {
        Query queryGetExpensesForSettlementId = em
                .createNamedQuery("getExpensesForSettlementId");
        queryGetExpensesForSettlementId.setParameter("settlementId", id);
        List<ExpenseReportDataBean> erdbList = new ArrayList<ExpenseReportDataBean>();
        Collection<Object[]> results = queryGetExpensesForSettlementId
                .getResultList();
        for (Object[] oa : results) {
            ExpenseReportDataBean erdb = new ExpenseReportDataBean();
            // Each object is a list
            erdb.setExpenseId(((Long) oa[0]).longValue());
            erdb.setExpenseAmount(((Float) oa[1]).floatValue());
            erdb.setExpenseDate(((Date) oa[2]));
            erdb.setExpenseDescription(((String) oa[3]));
            erdb.setPaidBy(((String) oa[4]));
            erdb.setUserName(((String) oa[5]));
            erdb.setUserShareAmount(((Float) oa[6]).floatValue());
            // set in list
            erdbList.add(erdb);
        }
        return erdbList;
    }

    @Transactional
    public int deleteSettlement(Long sid)
    {
        // 0 return code is good. 1 is bad
        int result = 0;
        try {
            // delete reports
            Query queryDeleteReportsForSid = em
                    .createNamedQuery("deleteReportsForSid");
            queryDeleteReportsForSid.setParameter("sid", sid);
            queryDeleteReportsForSid.executeUpdate();
            // reports deleted
            SettlementEntity se = em.find(SettlementEntity.class, sid);
            em.remove(se);
        } catch (Exception e) {
            result = 1;
            logger.error(
                    "Error occured while deleting settlement " + e.getMessage(),
                    e);
        }
        return result;
    }

}
