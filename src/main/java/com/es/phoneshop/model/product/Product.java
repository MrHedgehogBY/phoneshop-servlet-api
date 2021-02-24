package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.IdentifiableItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Product extends IdentifiableItem implements Serializable {

    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<ProductPrice> prices;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.prices = new ArrayList<>();
        this.prices.add(new ProductPrice(new Date(), price, currency));
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.prices = new ArrayList<>();
        this.prices.add(new ProductPrice(new Date(), price, currency));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ProductPrice> getPrices() {
        return prices;
    }

    public void addNewPrice(BigDecimal price, Currency currency) {
        this.price = price;
        this.currency = currency;
        this.prices.add(new ProductPrice(new Date(), price, currency));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getStock() == product.getStock() &&
                getId().equals(product.getId()) &&
                getCode().equals(product.getCode()) &&
                getDescription().equals(product.getDescription()) &&
                getPrice().equals(product.getPrice()) &&
                getCurrency().equals(product.getCurrency()) &&
                getImageUrl().equals(product.getImageUrl()) &&
                getPrices().equals(product.getPrices());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getDescription(),
                getPrice(), getCurrency(), getStock(), getImageUrl(), getPrices());
    }
}