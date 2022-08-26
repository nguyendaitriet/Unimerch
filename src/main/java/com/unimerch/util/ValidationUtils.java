package com.unimerch.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtils {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,128}$";
    public static final String ID_REGEX = "\\d+";

    public boolean isIdValid(String id) {
        return Pattern.matches(ID_REGEX, id);
    }

    public boolean isPasswordvalid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
