package com.es.phoneshop.model.recent;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayDeque;
import java.util.Optional;

public class DefaultRecentHistoryService implements RecentHistoryService {

    public static final String HISTORY_SESSION_ATTRIBUTE = DefaultRecentHistoryService.class + ".history";
    private int maxSize = 3;

    public static RecentHistoryService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final RecentHistoryService instance = new DefaultRecentHistoryService();
    }

    private DefaultRecentHistoryService() {

    }

    @Override
    public void add(RecentHistory history, Product product) {
        ArrayDeque<Product> recentProducts = history.getRecentProducts();
        Optional<Product> sameProduct = recentProducts.stream()
                .filter(item -> item.getDescription().equals(product.getDescription()))
                .findAny();
        if (sameProduct.isPresent()) {
            recentProducts.removeFirstOccurrence(product);
        }
        recentProducts.addFirst(product);
        if (recentProducts.size() > maxSize) {
            recentProducts.pollLast();
        }
    }
}
