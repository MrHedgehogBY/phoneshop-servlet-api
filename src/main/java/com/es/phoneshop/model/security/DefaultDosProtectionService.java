package com.es.phoneshop.model.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {

    private static final Long THRESHOLD = 20L;
    private static final Long ONE_MINUTE = 60000L;
    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, Long> timeMap = new ConcurrentHashMap<>();

    public static DosProtectionService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final DosProtectionService instance = new DefaultDosProtectionService();
    }

    private DefaultDosProtectionService() {

    }

    @Override
    public boolean isAllowed(String ip) {
        Long requestQuantity = countMap.get(ip);
        if (requestQuantity == null || requestQuantity == 0L) {
            requestQuantity = 1L;
            timeMap.put(ip, System.currentTimeMillis());
        } else {
            boolean available = isAvailable(timeMap, countMap, ip);
            if (available) {
                return true;
            } else {
                if (requestQuantity > THRESHOLD) {
                    return false;
                }
            }
            requestQuantity++;
        }
        countMap.put(ip, requestQuantity);
        return true;
    }

    private boolean isAvailable(Map<String, Long> timeMap, Map<String, Long> countMap, String ip) {
        if (System.currentTimeMillis() - timeMap.get(ip) > ONE_MINUTE) {
            countMap.put(ip, 0L);
            return true;
        } else {
            return false;
        }
    }
}
