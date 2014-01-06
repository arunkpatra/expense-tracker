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
