package com.es.phoneshop.dao;

import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public class ArrayListOrderDao extends AbstractDao<Order> implements OrderDao {

    public static OrderDao getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final OrderDao instance = new ArrayListOrderDao();
    }

    private ArrayListOrderDao() {

    }

    @Override
    public Order get(String secureId) {
        return lock.safeRead(() -> items.stream()
                .filter(order -> order.getSecureId().equals(secureId))
                .findAny()
                .orElseThrow(OrderNotFoundException::new)
        );
    }
}
