package com.fs.antitheftsdk.utils;


import android.text.TextUtils;
import android.util.Patterns;

/*This class include validation methods for registration*/
public class ValidationUtils {
    public static final String REGEX_MOBILE_NO = "^[0-9]{10,15}$";

    private ValidationUtils() {
    }

    public static boolean isValidEmail(String text) {
        if (!TextUtils.isEmpty(text)) {
            return Patterns.EMAIL_ADDRESS.matcher(text).matches();
        } else {
            return false;
        }
    }

    public static boolean isValidMobile(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text.matches(REGEX_MOBILE_NO);
        } else {
            return false;
        }
    }

}
