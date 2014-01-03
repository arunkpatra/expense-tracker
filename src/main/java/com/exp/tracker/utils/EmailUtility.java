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

package com.exp.tracker.utils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;
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

/**
 * An utility class that can send emails.
 * 
 * @author Arun Patra
 * 
 */
public class EmailUtility
{
    /**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(EmailUtility.class);

    /**
     * The SMTP host server of the email provider.
     */
    private String smtpHost;
    /**
     * The email account to be used to send emails.
     */
    private String userAccount;
    /**
     * The account password for the account used to send emails.
     */
    private String userPassword;
    /**
     * The account to display in the from field.
     */
    private String fromAccount;
    /**
     * The display name to be used.
     */
    private String fromName;

    /**
     * The transport type.
     * 
     */
    public static final String TRANSPORT_TYPE = "smtp";

    /**
     * Sends an email.
     * 
     * @param emailIdStrings
     *            A String array containing a list of email addresses.
     * @param emailSubject
     *            The subject of the email.
     * @param messageContent
     *            The message body.
     * @param emailAttachments
     *            A map containing any attachments. The key should be the file
     *            name. The value os a byte[] containing the binary
     *            representation of the attachment.
     * @throws EmailCommunicationException
     *             If any exception occurs.
     */
    public void sendEmail(String[] emailIdStrings, String emailSubject,
            String messageContent, Map<String, byte[]> emailAttachments)
            throws EmailCommunicationException
    {
        if (null == emailIdStrings) {
            throw new EmailCommunicationException(
                    "Null array passed to this method.");
        }
        if (emailIdStrings.length == 0) {
            throw new EmailCommunicationException(
                    "No email addresses were provided. Array was empty.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("About to send an email.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.starttls.enable", "true");

        try {

            Address[] toListArray = new Address[emailIdStrings.length];
            for (int i = 0; i < emailIdStrings.length; i++) {
                toListArray[i] = new InternetAddress(emailIdStrings[i]);
            }

            // Get a Session object
            Session session = Session.getInstance(props, null);
            session.setDebug(false);
            // Will handle multi part message.
            Multipart mp = new MimeMultipart();
            // create message
            Message msg = new MimeMessage(session);
            msg.setRecipients(Message.RecipientType.TO, toListArray);
            msg.setFrom(new InternetAddress(fromAccount, fromName));
            msg.setSubject(emailSubject);
            Calendar calendar = Calendar.getInstance();
            msg.setSentDate(calendar.getTime());
            //
            // set contents.
            // create and fill the first message part
            MimeBodyPart mailMessagePart = new MimeBodyPart();
            mailMessagePart.setContent(messageContent, "text/html");
            //
            mp.addBodyPart(mailMessagePart);

            // Handle attachments now
            if (null != emailAttachments) {
                for (String fileNameString : emailAttachments.keySet()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.setFileName(fileNameString);
                    attachmentPart.setDataHandler(new DataHandler(
                            new ByteArrayDataSource(emailAttachments
                                    .get(fileNameString),
                                    "application/octet-stream")));
                    mp.addBodyPart(attachmentPart);
                }
            }
            // add the Multi-part to the message
            msg.setContent(mp);
            // Create transport
            Transport transport = session.getTransport(TRANSPORT_TYPE);
            transport.connect(smtpHost, userAccount, userPassword);
            transport.sendMessage(msg, toListArray);
            //
            logger.info("Mail was sent succesfuly.");
        } catch (AddressException ae) {
            throw new EmailCommunicationException("Email address was invalid",
                    ae);
        } catch (MessagingException me) {
            throw new EmailCommunicationException("Error sending email.", me);
        } catch (UnsupportedEncodingException uee) {
            throw new EmailCommunicationException(
                    "Error sending email, unsupported encoding.", uee);
        }
    }

    /**
     * Sets the smtp host.
     * 
     * @param smtpHost
     */
    public void setSmtpHost(String smtpHost)
    {
        this.smtpHost = smtpHost;
    }

    /**
     * Sets the user account.
     * 
     * @param userAccount
     */
    public void setUserAccount(String userAccount)
    {
        this.userAccount = userAccount;
    }

    /**
     * Sets the user password.
     * 
     * @param userPassword
     */
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    /**
     * Sets the from account.
     * 
     * @param fromAccount
     */
    public void setFromAccount(String fromAccount)
    {
        this.fromAccount = fromAccount;
    }

    /**
     * Sets the from name.
     * 
     * @param fromName
     */
    public void setFromName(String fromName)
    {
        this.fromName = fromName;
    }
}
