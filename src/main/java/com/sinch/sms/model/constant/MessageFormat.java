package com.sinch.sms.model.constant;

import java.util.Arrays;
import java.util.Optional;

public enum MessageFormat {
    SMS("SMS");

    private final String code;

    MessageFormat(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Optional<MessageFormat> fromCode(String code) {
        return Arrays.stream(values())
                .filter(f -> f.code.equalsIgnoreCase(code))
                .findFirst();
    }
}
