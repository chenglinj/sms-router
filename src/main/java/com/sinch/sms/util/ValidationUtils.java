package com.sinch.sms.util;

import com.sinch.sms.model.constant.SmsConstants;
import org.springframework.util.StringUtils;

public class ValidationUtils {

    private ValidationUtils() {

    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber) || !phoneNumber.startsWith("+")) {
            throw new IllegalArgumentException("Phone number must start with '+' and country code.");
        }

        if (phoneNumber.startsWith(SmsConstants.AU_COUNTRY_CODE)) {
            // Australia: +61 followed by 9 digits
            if (!phoneNumber.matches(SmsConstants.AU_NUMBER_REGEX)) {
                throw new IllegalArgumentException("Invalid Australian phone number format. Should be +61 followed by 9 digits.");
            }
        } else if (phoneNumber.startsWith(SmsConstants.NZ_COUNTRY_CODE)) {
            // New Zealand: +64 followed by 8 or 9 digits
            if (!phoneNumber.matches(SmsConstants.NZ_NUMBER_REGEX)) {
                throw new IllegalArgumentException("Invalid New Zealand phone number format. Should be +64 followed by 9 digits.");
            }
        } else {
            // Global: must be at least 8 digits total
            if (!phoneNumber.matches(SmsConstants.GLOBAL_NUMBER_REGEX)) {
                throw new IllegalArgumentException("Invalid global phone number format. Should start with '+' followed by 8 to 15 digits.");
            }
        }
    }
}
