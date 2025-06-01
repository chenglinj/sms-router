package com.sinch.sms.service;

public interface OptOutService {

    void optOut(String phoneNumber);

    void cancelOptOut(String phoneNumber);

    boolean isOptedOut(String phoneNumber);
}
