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

package com.exp.tracker.data.entities;

public abstract class EncryptionHelper
{

    /**
     * Provide a encrypted version of a string.
     * 
     * @param plainTextString
     * @return
     */
    protected String encryptString(String plainTextString) {
        return plainTextString;
    }
    
    
    /**
     * Provide a plain-text version of an encrypted string.
     * 
     * @param plainTextString
     * @return
     */
    protected String decryptString(String encryptedString) {
        return encryptedString;
    }
    
    /**
     * Masks all but last four characters of a provided string.
     * @param originalString
     * @return
     */
    protected String maskAllButLastFourChars(String originalString) {
        return null;
    }
}
