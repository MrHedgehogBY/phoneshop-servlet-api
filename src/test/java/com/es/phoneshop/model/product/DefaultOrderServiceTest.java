package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DefaultOrderServiceTest {

    private OrderService orderService = DefaultOrderService.getInstance();
    private Cart cart = new Cart();
    private int quantity = 2;
    private Product firstProduct = new Product(1L, "test-product", "Samsung Galaxy S II", new BigDecimal(500), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    @Before
    public void setup() {
        cart.getCartItems().add(new CartItem(firstProduct, quantity));
        cart.setTotalCost(BigDecimal.valueOf(100L));
    }

    @After
    public void tearDown() {
        cart.getCartItems().clear();
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(cart);
        assertNotNull(order.getTotalCost());
    }

    @Test(expected = NullPointerException.class)
    public void testGetOrderEmptyCart() {
        cart.getCartItems().clear();
        cart.setTotalCost(null);
        Order order = orderService.getOrder(cart);
    }

    @Test
    public void testPlaceOrder() {
        orderDao.clear();
        Order order = orderService.getOrder(cart);
        orderService.placeOrder(order);
        assertNotNull(orderDao.get(order.getId()));
    }

    @Test
    public void testGetPaymentMethods() {
        List<PaymentMethod> list;
        list = orderService.getPaymentMethods();
        assertFalse(list.isEmpty());
    }
}
