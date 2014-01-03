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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An exception class for email operation failures.
 * 
 * @author Arun Patra
 * 
 */
public class EmailCommunicationException extends Exception
{
    /**
     * The logger.
     */
    private static final Log logger = LogFactory
            .getLog(EmailCommunicationException.class);
    /**
     * The default serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * No arg constructor.
     */
    public EmailCommunicationException() {
        super();
        logger.error("An error occured while sending or receiving email.");
    }

    /**
     * Constructor.
     * 
     * @param message
     *            The detailed message.
     * @param cause
     *            The cause.
     */
    public EmailCommunicationException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message, cause);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            The detailed cause.
     */
    public EmailCommunicationException(String message) {
        super(message);
        logger.error(message);
    }

}
