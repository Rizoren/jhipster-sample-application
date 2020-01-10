package com.mycompany.myapp.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "ru";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String NAMESPACE_URI = "http://localhost:8080";

    private Constants() {
    }
}
