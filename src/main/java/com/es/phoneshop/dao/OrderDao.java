package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order get(Long id);

    Order get(String secureId);

    void save(Order order);

    void clear();
}
