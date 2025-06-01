package com.sinch.sms.repository;

public interface OptOutRepository {

    void save(String phoneNumber);

    boolean exists(String phoneNumber);

    void delete(String phoneNumber);
}
