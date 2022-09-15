package com.unimerch.util;

import java.util.regex.Pattern;

public class ValidationUtils {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,128}$";
    public static final String ID_REGEX = "\\d+";

    public static boolean isIdValid(String id) {
        return Pattern.matches(ID_REGEX, id);
    }

    public static boolean isPasswordvalid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
