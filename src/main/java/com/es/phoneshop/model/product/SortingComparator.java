package com.es.phoneshop.model.product;

import java.util.Comparator;

public class SortingComparator implements Comparator<Product> {

    private SortField sortField;
    private SortOrder sortOrder;

    public SortingComparator(SortField sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Product product1, Product product2) {
        int value;
        if (sortField == SortField.description) {
            value = product1.getDescription().compareTo(product2.getDescription());
        } else {
            value = product1.getPrice().compareTo(product2.getPrice());
        }
        return sortOrder == SortOrder.asc ? value : -1 * value;
    }
}
