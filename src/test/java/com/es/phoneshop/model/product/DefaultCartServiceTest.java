package com.es.phoneshop.model.product;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class DefaultCartServiceTest {

    private CartService cartService = DefaultCartService.getInstance();
    private int quantity = 2;
    private int hugeQuantity = 1000;
    private Product firstProduct = new Product(1L, "test-product", "Samsung Galaxy S II", new BigDecimal(500), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private Product secondProduct = new Product(2L, "palmp", "Palm Pixi", new BigDecimal(170), Currency.getInstance("USD"), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg");
    private Cart cart = new Cart();

    @Before
    public void setup() {
        cart.getCartItems().add(new CartItem(firstProduct, quantity));
    }

    @After
    public void tearDown() {
        cart.getCartItems().clear();
    }

    @Test
    public void testAddSameItemToCart() throws OutOfStockException {
        int currentQuantity = cart.getCartItems().get(0).getQuantity();
        cartService.add(cart, firstProduct, quantity);
        assertEquals(cart.getCartItems().get(0).getQuantity() - currentQuantity, quantity);
    }

    @Test
    public void testAddNewItemToCart() throws OutOfStockException {
        int currentCartLength = cart.getCartItems().size();
        cartService.add(cart, secondProduct, quantity);
        assertEquals(cart.getCartItems().size() - currentCartLength, 1);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddSameOutOfStock() throws OutOfStockException {
        cartService.add(cart, secondProduct, hugeQuantity);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddNewOutOfStock() throws OutOfStockException {
        cartService.add(cart, firstProduct, hugeQuantity);
    }
}
