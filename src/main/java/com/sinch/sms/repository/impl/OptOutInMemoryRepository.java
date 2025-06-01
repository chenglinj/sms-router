package com.sinch.sms.repository.impl;

import com.sinch.sms.repository.OptOutRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OptOutInMemoryRepository implements OptOutRepository {

    private final Set<String> optOutNumberSet = ConcurrentHashMap.newKeySet();

    @Override
    public void save(String phoneNumber) {
        optOutNumberSet.add(phoneNumber);
    }

    @Override
    public boolean exists(String phoneNumber) {
        return optOutNumberSet.contains(phoneNumber);
    }

    @Override
    public void delete(String phoneNumber) {
        optOutNumberSet.remove(phoneNumber);
    }
}
