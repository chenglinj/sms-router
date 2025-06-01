package com.sinch.sms.service.impl;

import com.sinch.sms.repository.impl.OptOutInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OptOutServiceImplTest {

    private OptOutServiceImpl optOutService;

    @BeforeEach
    void setup() {
        OptOutInMemoryRepository optOutRepository = new OptOutInMemoryRepository();
        this.optOutService = new OptOutServiceImpl(optOutRepository);

    }

    @Test
    void testOptOut() {
        String phone = "+61400000000";
        optOutService.optOut(phone);
        assertTrue(optOutService.isOptedOut(phone));
    }

    @Test
    void testCancelOptOut() {
        String phone = "+61400000000";
        optOutService.optOut(phone);
        optOutService.cancelOptOut(phone);
        assertFalse(optOutService.isOptedOut(phone));
    }

}
