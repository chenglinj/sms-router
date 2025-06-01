package com.sinch.sms.repository.impl;

import com.sinch.sms.model.entity.Message;
import com.sinch.sms.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MessageInMemoryRepository implements MessageRepository {

    private final Map<String, Message> messageInMemoryStore = new ConcurrentHashMap<>();

    @Override
    public void save(Message message) {
        messageInMemoryStore.put(message.getId(), message);
    }

    @Override
    public Message findById(String id) {
        return messageInMemoryStore.get(id);
    }

    @Override
    public Collection<Message> findAll() {
        return messageInMemoryStore.values();
    }
}
