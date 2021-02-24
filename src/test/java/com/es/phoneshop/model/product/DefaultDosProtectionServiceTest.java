package com.es.phoneshop.model.product;

import com.es.phoneshop.model.security.DefaultDosProtectionService;
import com.es.phoneshop.model.security.DosProtectionService;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultDosProtectionServiceTest {

    private DosProtectionService protectionService = DefaultDosProtectionService.getInstance();
    private Long quantityToCheck = 30L;
    private String testIp = "111.111.111.11";
    private String testIpAfterDos = "111.111.111.12";

    @Test
    public void testIsAllowed() {
        boolean checker = protectionService.isAllowed(testIp);
        assertTrue(checker);
    }

    @Test
    public void testIsAllowedAfterDos() {
        boolean checker = true;
        for (int i = 0; i < quantityToCheck; i++) {
            checker = protectionService.isAllowed(testIpAfterDos);
        }
        assertFalse(checker);
    }
}
