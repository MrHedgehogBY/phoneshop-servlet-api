package com.es.phoneshop.model.recent;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayDeque;

public class RecentHistory {

    private ArrayDeque<Product> recentProducts;

    public RecentHistory() {
        recentProducts = new ArrayDeque<>();
    }

    public ArrayDeque<Product> getRecentProducts() {
        return recentProducts;
    }
}
