package com.exp.tracker.services.impl;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
     * The email utility.
     */
    private EmailUtility emailUtility;

    /**
     * Sends password reset email.
     * 
     * @param ub
     *            The user bean.
     */
    public void sendPasswordResetEmail0(UserBean ub)
    {
        // The email id array.
        String[] emailIdStrings = { ub.getEmailId() };
        // Email subject.
        String emailSubject = "Your New Expense Tracker Password has been Reset.";
        // Message Content
        String messageContent = getPasswordResetMessageText(ub);

        try {
            emailUtility.sendEmail(emailIdStrings, emailSubject,
                    messageContent, null);
        } catch (EmailCommunicationException e) {
            logger.error("Failed to send password reset email.", e);
        }
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
    public void sendSettlementNotice(SettlementBean sb, List<UserBean> ul,
            byte[] settlementReport, byte[] expenseReport)
    {
        // The email id array.
        String[] emailIdStrings = new String[ul.size()];
        for (int i = 0; i < emailIdStrings.length; i++) {
            emailIdStrings[i] = ul.get(i).getEmailId();
        }
        // Email subject.
        String emailSubject = "New Settlement generated.";
        // Message Content
        String messageContent = getSettlementNoticeText(sb, ul);
        // Attachment Map
        Map<String, byte[]> emailAttachments = new HashMap<String, byte[]>();
        emailAttachments.put("SettlementReport.pdf", settlementReport);
        emailAttachments.put("ExpenseReport.pdf", expenseReport);
        try {
            emailUtility.sendEmail(emailIdStrings, emailSubject,
                    messageContent, emailAttachments);
        } catch (EmailCommunicationException e) {
            logger.error("Failed to send settlement notice email.", e);
        }
    }

    /**
     * Sends welcome email.
     * 
     * @param ub
     */
    public void sendWelcomeEmail0(UserBean ub)
    {
        // The email id array.
        String[] emailIdStrings = { ub.getEmailId() };
        // Email subject.
        String emailSubject = "Your New Expense Tracker User Id.";
        // Message Content
        String messageContent = getWelcomeMessageText(ub);

        try {
            emailUtility.sendEmail(emailIdStrings, emailSubject,
                    messageContent, null);
        } catch (EmailCommunicationException e) {
            logger.error("Failed to send welcome email.", e);
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
     * Create the content for the password reset email.
     * 
     * @param ub
     *            The suer bean.
     * @return The content for the email.
     */
    private String getPasswordResetMessageText(UserBean ub)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Dear ").append(ub.getFirstName()).append(",").append("\n\n");
        sb.append(
                "Your Expense Tracker password has been reset. "
                        + "It is recommended that you change your password "
                        + "after you logon using your temporary password mentioned below.")
                .append("\n\n");
        sb.append("User Id:").append("\t").append(ub.getUsername())
                .append("\n");
        sb.append("Password:  ").append("\t").append(ub.getPassword())
                .append("\n");
        sb.append("\n");
        sb.append("Have a great day!.").append("\n\n");
        sb.append("Thank You,").append("\n").append("Expense Tracker Admin");
        return sb.toString();
    }

    /**
     * Create Settlement notice email body.
     * 
     * @param s
     *            The settlement bean
     * @param ul
     *            The user bean list
     * @return The email body
     */
    private String getSettlementNoticeText(SettlementBean s, List<UserBean> ul)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body>"
                + "<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
                + "<tr>"
                + "<td width=\"582\"><br/><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"> <b>Dear ");
        sb.append("Expense Tracker User,");
        sb.append("<br><br></b>Warm Greetings to you!"
                + "<br><br>A new settlement has been created for the following period:<br><br><b>Start Date:</b> ");
        sb.append(df.format(s.getStartDate()));
        sb.append("<br><b>End Date:</b> ");
        sb.append(df.format(s.getEndDate()));
        sb.append("<br><br>Please logon to Expense Tracker to see your payables/recievables.<br>"
                + "<br>Thank You,<br>Expense Tracker Admin<br></td></tr></table></body></html>");
        return sb.toString();
    }

    /**
     * Welcome message text.
     * 
     * @param ub
     *            The user bean
     * @return The body of the email message.
     */
    private String getWelcomeMessageText(UserBean ub)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Dear ").append(ub.getFirstName()).append(",").append("\n\n");
        sb.append(
                "Your new Expense Tracker user id has been created. "
                        + "It is recommended that you change your password after you logon for the first time.")
                .append("\n\n");
        sb.append("User Id:").append("\t").append(ub.getUsername())
                .append("\n");
        sb.append("Password:  ").append("\t").append(ub.getPassword())
                .append("\n");
        sb.append("\n");
        sb.append("Have a great day!.").append("\n\n");
        sb.append("Thank You,").append("\n").append("Expense Tracker Admin");
        return sb.toString();
    }
}
