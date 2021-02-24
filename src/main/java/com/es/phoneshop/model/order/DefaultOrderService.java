package com.es.phoneshop.model.order;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {

    private OrderDao orderDao;

    public static OrderService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final OrderService instance = new DefaultOrderService();
    }

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setOrderItems(cart.getCartItems().stream().map(cartItem -> {
            try {
                return (CartItem) cartItem.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getDeliveryCost().add(order.getSubtotal()));
        order.setOrderDetails(new OrderDetails());
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(25);
    }
}
