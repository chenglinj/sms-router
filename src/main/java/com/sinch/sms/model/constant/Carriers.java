package com.sinch.sms.model.constant;

public enum Carriers {
    TELSTRA("Telstra"),
    OPTUS("Optus"),
    SPARK("Spark"),
    GLOBAL("Global");

    private final String code;

    Carriers(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
