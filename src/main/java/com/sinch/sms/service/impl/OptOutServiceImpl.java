package com.sinch.sms.service.impl;

import com.sinch.sms.repository.OptOutRepository;
import com.sinch.sms.service.OptOutService;
import com.sinch.sms.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptOutServiceImpl implements OptOutService {

    private final OptOutRepository optOutRepository;

    @Autowired
    public OptOutServiceImpl(OptOutRepository optOutRepository) {
        this.optOutRepository = optOutRepository;
    }

    @Override
    public void optOut(String phoneNumber) {
        ValidationUtils.validatePhoneNumber(phoneNumber);

        if (optOutRepository.exists(phoneNumber)) {
            throw new IllegalArgumentException(String.format("Phone number %s has already been opted out.", phoneNumber));
        }

        optOutRepository.save(phoneNumber);
    }

    @Override
    public void cancelOptOut(String phoneNumber) {
        ValidationUtils.validatePhoneNumber(phoneNumber);

        if (!optOutRepository.exists(phoneNumber)) {
            throw new IllegalArgumentException(String.format("Phone number %s has not been opted out.", phoneNumber));
        }

        optOutRepository.delete(phoneNumber);
    }

    @Override
    public boolean isOptedOut(String phoneNumber) {
        return optOutRepository.exists(phoneNumber);
    }
}
