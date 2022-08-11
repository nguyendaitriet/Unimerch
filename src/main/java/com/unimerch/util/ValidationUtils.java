package com.unimerch.util;

public class ValidationUtils {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,}$";
    public static final String ID_REGEX = "\\d+";

    public static final String VALID_PASSWORD = "Password: Minimum five characters, at least one letter, one number and one special character.";
    public static final String ID_NOT_EXIST = "ID doesn't exist. Try again.";
    public static final String USERNAME_EXISTS = "Username has already existed.";
    public static final String SERVER_ERROR = "Server error. Try again!";
    public static final String NO_DATA_FOUND = "Data not found. Try again!";
    public static final String NOT_ALLOW = "Sorry, you cannot do this action.";


}
