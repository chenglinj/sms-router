package com.sinch.sms.model.constant;

public class SmsConstants {

    private SmsConstants() {
    }

    public static final String AU_COUNTRY_CODE = "+61";
    public static final String NZ_COUNTRY_CODE = "+64";

    public static final String AU_NUMBER_REGEX = "\\+61\\d{9}";
    public static final String NZ_NUMBER_REGEX = "\\+64\\d{8,9}";
    public static final String GLOBAL_NUMBER_REGEX = "\\+\\d{8,15}";

    public static final int MAX_SMS_LENGTH = 160;
}
