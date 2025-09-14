package com.flight.api.validation;

public class ValidationConstants {

    public static final String EMAIL_NOT_BLANK = "Email cannot be empty";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String PASSWORD_NOT_BLANK = "Password cannot be empty";
    public static final String PASSWORD_MIN_LENGTH = "Password must be at least 6 characters long";
    public static final String FIRST_NAME_NOT_BLANK = "First name cannot be empty";
    public static final String LAST_NAME_NOT_BLANK = "Last name cannot be empty";
    public static final String COUNTRY_NOT_BLANK = "Country cannot be empty";
    public static final String PHONE_NOT_BLANK = "Phone number cannot be empty";

    private ValidationConstants() {
        // Private constructor to prevent instantiation
    }
} 