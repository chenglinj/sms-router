package com.sinch.sms.repository;

import com.sinch.sms.model.entity.Message;

import java.util.Collection;

public interface MessageRepository {

    void save(Message message);

    Message findById(String id);

    Collection<Message> findAll();
}
