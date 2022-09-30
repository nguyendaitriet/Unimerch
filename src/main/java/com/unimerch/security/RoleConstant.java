package com.unimerch.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RoleConstant {

    public static final String CODE_ADMIN = "MANAGER";
    public static final String CODE_USER = "USER";
    public static final String AUTHENTICATED_USER = "isAuthenticated()";
    public static final String PREFIX="hasAnyAuthority('";
    public static final String SUFFIX="')";
    public static final String MIDDLE="','";

    public static final String MANAGER_USER_AUTHORIZATION = PREFIX+ CODE_ADMIN + MIDDLE + CODE_USER + SUFFIX;
    public static final String MANAGER_AUTHORIZATION = PREFIX+ CODE_ADMIN  + SUFFIX;
    public static final String USER_AUTHORIZATION = PREFIX+ CODE_USER + SUFFIX;

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(MANAGER_USER_AUTHORIZATION)
    public @interface ManagerUserAuthorization {}

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(MANAGER_AUTHORIZATION)
    public @interface ManagerAuthorization {}

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(USER_AUTHORIZATION)
    public @interface UserAuthorization {}

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(AUTHENTICATED_USER)
    public @interface AuthenticatedUser {}


}
