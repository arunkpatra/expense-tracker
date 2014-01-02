package com.exp.tracker.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;

import com.exp.tracker.data.model.SettlementBean;
import com.exp.tracker.data.model.UserBean;

public class EmailMessageHelper implements IEmailMessageSender
{

    /**
     * The logger.
     */
    private static final Log logger = LogFactory
            .getLog(EmailMessageHelper.class);

    private String _smtpHost;
    private String _user;
    private String _password;
    private String _from;
    private String _fromName;

    private Properties props;
    public static final int _port = 587;
    public static final String TRANSPORT_TYPE = "smtp";

    public EmailMessageHelper() {
    }

    @Async
    public void sendSettlementNotice(SettlementBean sb, List<UserBean> ul,
            byte[] settlementReport, byte[] expenseReport)
    {
        if (logger.isDebugEnabled()) {
            logger.debug("About to send settlement notice.");
        }
        this.props = new Properties();
        props.put("mail.smtp.host", _smtpHost);
        props.put("mail.smtp.starttls.enable", "true");
        int result = 0;
        try {
            // Get a Session object
            Session session = Session.getInstance(props, null);
            session.setDebug(false);
            // create message
            Message msg = new MimeMessage(session);
            List<Address> toAddressList = new ArrayList<Address>();
            for (UserBean ub : ul) {
                if (null != ub.getEmailId()) {
                    if (!"".equalsIgnoreCase(ub.getEmailId())) {
                        Address adr;
                        adr = new InternetAddress(ub.getEmailId());
                        toAddressList.add(adr);
                    }
                }
            }
            if (toAddressList.size() != 0) {
                Address[] toListArray = new Address[toAddressList.size()];// =
                // (Address[])
                // toAddressList.toArray();
                int i = 0;
                for (Address a : toAddressList) {
                    toListArray[i] = a;
                    i += 1;
                }
                //
                msg.setRecipients(Message.RecipientType.TO, toListArray);
                msg.setFrom(new InternetAddress(_from, _fromName));
                msg.setSubject("New Settlement generated.");
                Calendar calendar = Calendar.getInstance();
                msg.setSentDate(calendar.getTime());
                //
                // set contents.
                // create and fill the first message part
                MimeBodyPart mailMessagePart = new MimeBodyPart();

                mailMessagePart.setContent(getMessageText(sb, ul), "text/html");

                // handle the settlement attachment now
                MimeBodyPart mailSettlementReportAttachmentPart = new MimeBodyPart();
                mailSettlementReportAttachmentPart
                        .setDataHandler(new DataHandler(
                                new ByteArrayDataSource(settlementReport,
                                        "application/octet-stream")));
                mailSettlementReportAttachmentPart
                        .setFileName("SettlementReport.pdf");
                // handle the expense attachment now
                MimeBodyPart mailExpenseReportAttachmentPart = new MimeBodyPart();
                mailExpenseReportAttachmentPart.setDataHandler(new DataHandler(
                        new ByteArrayDataSource(expenseReport,
                                "application/octet-stream")));
                mailExpenseReportAttachmentPart
                        .setFileName("ExpenseReport.pdf");

                // create the Multipart and add its parts to it
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mailMessagePart);
                mp.addBodyPart(mailSettlementReportAttachmentPart);
                mp.addBodyPart(mailExpenseReportAttachmentPart);

                // add the Multipart to the message
                msg.setContent(mp);

                Transport transport = session.getTransport(TRANSPORT_TYPE);
                transport.connect(_smtpHost, _user, _password);
                transport.sendMessage(msg, toListArray);
                //
                logger.info("Mail was sent succesfuly.");
            } else {
                logger.info("No email ids to send email.");
            }
        } catch (AddressException ae) {
            logger.error("Error occured while sending email.", ae);            
            result = 1;
        } catch (MessagingException me) {
            logger.error("Error occured while sending email.", me);           
            result = 1;
        } catch (UnsupportedEncodingException uee) {
            logger.error("Error occured while sending email.", uee);           
            result = 1;
        }

        // return result;
    }

    private String getMessageText(SettlementBean s, List<UserBean> ul)
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

    @Async
    public void sendPasswordResetEmail(UserBean ub)
    {
        this.props = new Properties();
        props.put("mail.smtp.host", _smtpHost);
        props.put("mail.smtp.starttls.enable", "true");
        int result = 0;
        try {
            Session session = Session.getInstance(props, null);
            session.setDebug(false);
            // create message
            Message msg = new MimeMessage(session);
            List<Address> toAddressList = new ArrayList<Address>();

            Address adr;
            adr = new InternetAddress(ub.getEmailId());
            toAddressList.add(adr);

            Address[] toListArray = new Address[toAddressList.size()];// =
            int i = 0;
            for (Address a : toAddressList) {
                toListArray[i] = a;
                i += 1;
            }
            //
            msg.setRecipients(Message.RecipientType.TO, toListArray);
            msg.setFrom(new InternetAddress(_from, _fromName));
            msg.setSubject("Your New Expense Tracker Password has been Reset.");
            Calendar calendar = Calendar.getInstance();
            msg.setSentDate(calendar.getTime());
            //
            // set contents.
            msg.setText(getPasswordResetMessageText(ub));
            //
            Transport transport = session.getTransport(TRANSPORT_TYPE);
            transport.connect(_smtpHost, _user, _password);
            transport.sendMessage(msg, toListArray);
            //
            System.out.println("\nPassword Rest Mail was sent successfully.");
        } catch (AddressException ae) {
            logger.error("Error occured while sending password reset email.", ae);           
            result = 1;
        } catch (MessagingException me) {
            logger.error("Error occured while sending password reset email.", me);
            result = 1;
        } catch (UnsupportedEncodingException uee) {
            logger.error("Error occured while sending password reset email.", uee);
            result = 1;
        }
        // return result;
    }

    @Async
    public void sendWelcomeEmail(UserBean ub)
    {
        this.props = new Properties();
        props.put("mail.smtp.host", _smtpHost);
        props.put("mail.smtp.starttls.enable", "true");
        int result = 0;
        try {
            Session session = Session.getInstance(props, null);
            session.setDebug(false);
            // create message
            Message msg = new MimeMessage(session);
            List<Address> toAddressList = new ArrayList<Address>();

            Address adr;
            adr = new InternetAddress(ub.getEmailId());
            toAddressList.add(adr);

            Address[] toListArray = new Address[toAddressList.size()];// =
            // (Address[])
            // toAddressList.toArray();
            int i = 0;
            for (Address a : toAddressList) {
                toListArray[i] = a;
                i += 1;
            }
            //
            msg.setRecipients(Message.RecipientType.TO, toListArray);
            msg.setFrom(new InternetAddress(_from, _fromName));
            msg.setSubject("Your New Expense Tracker User Id.");
            Calendar calendar = Calendar.getInstance();
            msg.setSentDate(calendar.getTime());
            //
            // set contents.
            msg.setText(getWelcomeMessageText(ub));
            //
            Transport transport = session.getTransport(TRANSPORT_TYPE);
            transport.connect(_smtpHost, _user, _password);
            transport.sendMessage(msg, toListArray);
            //
            System.out.println("\nMail was sent successfully.");
        } catch (AddressException ae) {
            logger.error("Error occured while sending welcome email.", ae);
            result = 1;
        } catch (MessagingException me) {
            logger.error("Error occured while sending welcome email.", me);
            result = 1;
        } catch (UnsupportedEncodingException uee) {
            logger.error("Error occured while sending welcome email.", uee);
            result = 1;
        }
        // return result;
    }

    public String get_smtpHost()
    {
        return _smtpHost;
    }

    public void set_smtpHost(String smtpHost)
    {
        _smtpHost = smtpHost;
    }

    public String get_user()
    {
        return _user;
    }

    public void set_user(String user)
    {
        _user = user;
    }

    public String get_password()
    {
        return _password;
    }

    public void set_password(String password)
    {
        _password = password;
    }

    public String get_from()
    {
        return _from;
    }

    public void set_from(String from)
    {
        _from = from;
    }

    public String get_fromName()
    {
        return _fromName;
    }

    public void set_fromName(String fromName)
    {
        _fromName = fromName;
    }
}
