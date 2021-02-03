package com.es.phoneshop.model.recent;

import com.es.phoneshop.model.product.Product;

public interface RecentHistoryService {
    void add(RecentHistory history, Product product);
}
