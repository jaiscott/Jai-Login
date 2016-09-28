package com.zohologin.demo.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jaivignesh.m.jt on 9/24/2016.
 */
public class CheckValue {

    public static boolean isValid(String value) {
        if (value != null && value != "") {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPsswordValid(String password) {
        boolean isValid = false;

        String expression = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])[a-zA-Z0-9@#$%]{6,20})";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
