package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ArrayListOrderDaoTest {

    private OrderDao orderDao = ArrayListOrderDao.getInstance();
    private Order order = new Order();
    private Order newOrder = new Order();
    private Long testId = 2L;
    private String testSecureId = UUID.randomUUID().toString();

    @Before
    public void setup() {
        order.setSecureId(UUID.randomUUID().toString());
        newOrder.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @After
    public void tearDown() {
        orderDao.clear();
    }

    @Test
    public void testGetOrderById() {
        Order orderFromDao = orderDao.get(order.getId());
        assertEquals(orderFromDao.getId(), order.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOrderByIdIncorrect() {
        Order orderFromDao = orderDao.get(testId);
    }

    @Test
    public void testGetOrderBySecureId() {
        Order orderFromDao = orderDao.get(order.getSecureId());
        assertEquals(orderFromDao.getSecureId(), order.getSecureId());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderBySecureIdIncorrect() {
        Order orderFromDao = orderDao.get(testSecureId);
    }

    @Test
    public void testSaveNewOrder() {
        String newOrderSecureId = newOrder.getSecureId();
        orderDao.save(newOrder);
        assertEquals(orderDao.get(newOrderSecureId), newOrder);
    }

    @Test
    public void testSaveExistingOrder() {
        BigDecimal totalCostOrder = order.getTotalCost();
        Long orderId = order.getId();
        order.setTotalCost(BigDecimal.valueOf(100L));
        orderDao.save(order);
        assertNotEquals(orderDao.get(orderId).getTotalCost(), totalCostOrder);
    }

    @Test(expected = NoSuchElementException.class)
    public void testSaveOrderIncorrectId() {
        newOrder.setId(testId);
        orderDao.save(newOrder);
    }
}
