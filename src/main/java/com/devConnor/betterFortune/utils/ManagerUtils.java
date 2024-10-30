package com.devConnor.betterFortune.utils;

public class ManagerUtils {

    public static Double deriveDoubleFromString(String valueStr) {
        try {
            return Double.parseDouble(valueStr);
        } catch (Exception e) {
            return null;
        }
    }
}
