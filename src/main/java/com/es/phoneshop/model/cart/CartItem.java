package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable, Cloneable {

    private Product product;
    private int quantity;
    private BigDecimal priceForQuantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        priceForQuantity = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceForQuantity() {
        return priceForQuantity;
    }

    public void setPriceForQuantity(BigDecimal priceForQuantity) {
        this.priceForQuantity = priceForQuantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product.getCode() +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
