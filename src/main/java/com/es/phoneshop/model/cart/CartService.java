package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

public interface CartService {
    void add(Cart cart, Product product, int quantity) throws OutOfStockException;
}
