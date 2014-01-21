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
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

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
     * The account to display in the from field.
     */
    private String fromAccount;
    /**
     * The display name to be used.
     */
    private String fromName;
    
    private JavaMailSender javaMailSender;

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
        
    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom(fromAccount, fromName);
			InternetAddress[] toListArray = new InternetAddress[emailIdStrings.length];
            for (int i = 0; i < emailIdStrings.length; i++) {
                toListArray[i] = new InternetAddress(emailIdStrings[i]);
            }
            //To
			helper.setTo(toListArray);
			//Subject
			helper.setSubject(emailSubject);
			//Body
			helper.setText(messageContent, true);
			//Attachments
			if (null != emailAttachments) {
				Set<String> attachmentFileNames = emailAttachments.keySet();				
				for (String fileName : attachmentFileNames) {
					helper.addAttachment(fileName, new ByteArrayDataSource(emailAttachments
                            .get(fileName),
                            "application/octet-stream"));
				}
			}

			javaMailSender.send(mimeMessage);
			System.out.println("Mail sent successfully.");
		} catch (MessagingException e) {
			throw new EmailCommunicationException("Error sending email.", e);
		} catch (UnsupportedEncodingException e) {
			throw new EmailCommunicationException("Error sending email. Unsupported encoding.", e);
		}
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

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
}
