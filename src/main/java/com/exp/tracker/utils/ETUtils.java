package com.exp.tracker.utils;

public class ETUtils
{

    public static final boolean areFloatsEqual(float f1, float f2) {        
        return (Math.abs(f1 - f2) < 0.0000001);       
    }
}
