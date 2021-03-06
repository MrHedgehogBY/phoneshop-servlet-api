package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product get(Long id);

    List<Product> findProducts();

    List<Product> findProducts(String search, SortField sortField, SortOrder sortOrder);

    List<Product> advancedSearch(String search, String searchMethod, BigDecimal minPrice, BigDecimal maxPrice);

    void save(Product product);

    void delete(Long id);

    void clear();
}
