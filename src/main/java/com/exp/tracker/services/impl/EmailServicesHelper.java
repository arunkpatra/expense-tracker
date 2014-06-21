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

import java.io.StringWriter;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;
import com.exp.tracker.utils.EmailCommunicationException;
import com.exp.tracker.utils.EmailUtility;

/**
 * A helper class for the email services required by the expense tracker app.
 * 
 * @author Arun Patra
 * 
 */
public abstract class EmailServicesHelper
{
    /**
     * The logger.
     */
    private static final Log logger = LogFactory
            .getLog(EmailServicesHelper.class);

    /**
     * The velocity engine.
     */
    private VelocityEngine velocityEngine;

    /**
     * The email utility.
     */
    private EmailUtility emailUtility;

    /**
     * Sends password reset email.
     * 
     * @param ub
     *            The user bean.
     */
    protected void sendPasswordResetEmail0(UserBean ub)
    {
        // The email id array.
        String[] emailIdStrings = { ub.getEmailId() };
        // Email subject.
        String emailSubject = "Your New Expense Tracker Password has been Reset.";
        // Message Content
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", ub);

        sendEmailInternal(emailIdStrings, emailSubject,
                "com/exp/tracker/email/templates/velocity/password-reset.vm",
                model, null);
    }

    /**
     * Sends settlement notice.
     * 
     * @param sb
     *            The settlement bean.
     * @param ul
     *            The user bean list.
     * @param settlementReport
     *            The settlement report pdf bytes.
     * @param expenseReport
     *            The expense report PDF bytes.
     */
    protected void sendSettlementNotice(SettlementBean sb, List<UserBean> ul,
            byte[] settlementReport, byte[] expenseReport)
    {
        // Attachment Map
        Map<String, byte[]> emailAttachments = new HashMap<String, byte[]>();
        emailAttachments.put("SettlementReport.pdf", settlementReport);
        emailAttachments.put("ExpenseReport.pdf", expenseReport);
        
        for (UserBean ub : ul) {
            // The email id array.
            String[] emailIdStrings = { ub.getEmailId() };
            // Email subject.
            String emailSubject = "New Settlement generated.";
            // Message Content
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("user", ub);
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String startDate = df.format(sb.getStartDate());
            String endDate = df.format(sb.getEndDate());
            model.put("startDate", startDate);
            model.put("endDate", endDate);
            
            sendEmailInternal(emailIdStrings, emailSubject,
                    "com/exp/tracker/email/templates/velocity/settlement-notice.vm",
                    model, emailAttachments);
        }
    }

    /**
     * Sends welcome email.
     * 
     * @param ub
     */
    protected void sendWelcomeEmail0(UserBean ub)
    {
        // The email id array.
        String[] emailIdStrings = { ub.getEmailId() };
        // Email subject.
        String emailSubject = "Your New Expense Tracker User Id.";

        // Message Content
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", ub);

        sendEmailInternal(emailIdStrings, emailSubject,
                "com/exp/tracker/email/templates/velocity/welcome-message.vm",
                model, null);
    }

    /**
     * Send email.
     * 
     * @param emailIdStrings
     *            A list of email Ids.
     * @param emailSubject
     *            The email subject.
     * @param emailTemplate
     *            The email template.
     * @param model
     *            The map which contains data.
     * @param emailAttachments
     *            The email attachments.
     */
    private void sendEmailInternal(String[] emailIdStrings,
            String emailSubject, String emailTemplate,
            Map<String, Object> model, Map<String, byte[]> emailAttachments)
    {
        StringWriter sWriter = new StringWriter();
        VelocityEngineUtils.mergeTemplate(velocityEngine, emailTemplate, "UTF-8",
                model, sWriter);
        try {
            emailUtility.sendEmail(emailIdStrings, emailSubject,
                    sWriter.toString(), emailAttachments);
        } catch (EmailCommunicationException e) {
            logger.error("Failed to send email.", e);
        }
    }

    /**
     * Sets the email util.
     * 
     * @param emailUtil
     */
    public void setEmailUtility(EmailUtility emailUtility)
    {
        this.emailUtility = emailUtility;
    }

    /**
     * Sets velocity engine.
     * 
     * @param velocityEngine
     */
    public void setVelocityEngine(VelocityEngine velocityEngine)
    {
        this.velocityEngine = velocityEngine;
    }
}
