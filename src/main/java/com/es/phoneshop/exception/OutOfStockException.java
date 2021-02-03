package com.es.phoneshop.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception {

    private Product product;
    private int requiredQuantity;
    private int availableStock;

    public OutOfStockException(Product product, int requiredQuantity, int availableStock) {
        this.product = product;
        this.requiredQuantity = requiredQuantity;
        this.availableStock = availableStock;
    }

    public Product getProduct() {
        return product;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
