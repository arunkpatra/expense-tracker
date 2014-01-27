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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.exp.tracker.data.entities.SettlementEntity;
import com.exp.tracker.data.entities.UserEntity;
import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.services.api.EmailService;

/**
 * Provides email services for the application.
 * 
 * @author Arun Patra
 *
 */
@Service("emailService")
@Repository
public class JavaMailEmailService extends EmailServicesHelper implements
        EmailService
{

    /**
     * The logger.
     */
    private static final Log logger = LogFactory
            .getLog(JavaMailEmailService.class);

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em)
    {
        this.em = em;
    }

    /**
     * Sends the settlement email.
     */
    @SuppressWarnings("unchecked")
    @Async
    public void sendSettlementEmail(Long sid, byte[] settlementReport,
            byte[] expenseReport)
    {
        //int emailSendResult = 0;
        // do not send email if sid is not valid
        if (sid != 0l) {
            Query queryGetSettlementForId = em
                    .createNamedQuery("getSettlementForId");
            queryGetSettlementForId.setParameter("id", sid);
            SettlementEntity se = (SettlementEntity) queryGetSettlementForId
                    .getSingleResult();
            SettlementBean sb = new SettlementBean(se);
            //
            Query querygetAllUsers = em.createNamedQuery("getAllUsers");
            List<UserEntity> uel = querygetAllUsers.getResultList();
            List<UserBean> ubl = new ArrayList<UserBean>();
            for (UserEntity ue : uel) {
                if (UserEntity.USER_ENABLED == ue.getEnabled()
                        && null != ue.getEmailId()) {
                    if (!("".equalsIgnoreCase(ue.getEmailId()))) {
                        ubl.add(new UserBean(ue));
                    }
                }
            }
            // send email
            sendSettlementNotice(sb, ubl, settlementReport, expenseReport);
        }
//        if (emailSendResult == 0) {
//            logger.info("Email was sent succesfuly.");
//        } else {
//            logger.error("Email not sent.");
//        }
       // return emailSendResult;
    }

    /**
     * Send the welcome email.
     */
    @Async
    public void sendWelcomeEmail(UserBean ub)
    {
       // @SuppressWarnings("unused")
        //int result = 0;
        if (null != ub) {
            if (null != ub.getEmailId()) {
                if (!"".equalsIgnoreCase(ub.getEmailId())) {
                    sendWelcomeEmail0(ub);
                }
            }
        }
    }

    /**
     * Sends the password reset email.
     */
    @Async
    public void sendPasswordResetEmail(UserBean ub)
    {
       // @SuppressWarnings("unused")
        //int result = 0;
        if (null != ub.getEmailId()) {
            if (!"".equalsIgnoreCase(ub.getEmailId())) {
                sendPasswordResetEmail0(ub);
            }
        }
    }
}
