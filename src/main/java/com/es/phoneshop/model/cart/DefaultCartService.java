package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import java.util.Optional;

public class DefaultCartService implements CartService {

    public static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static DefaultCartService instance;

    public static DefaultCartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    private DefaultCartService() {

    }

    @Override
    public void add(Cart cart, Product product, int quantity) throws OutOfStockException {
        Optional<CartItem> item = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getCode().equals(product.getCode()))
                .findAny();
        if (item.isPresent()) {
            increaseNumberOfExistingItem(item.get(), quantity, product);
        } else {
            addNewItemToCart(cart, quantity, product);
        }
    }

    private void increaseNumberOfExistingItem(CartItem item, int quantity, Product product) throws OutOfStockException {
        int itemQuantity = item.getQuantity();
        if (itemQuantity + quantity <= product.getStock()) {
            item.setQuantity(itemQuantity + quantity);
        } else {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    private void addNewItemToCart(Cart cart, int quantity, Product product) throws OutOfStockException {
        if (quantity <= product.getStock()) {
            cart.getCartItems().add(new CartItem(product, quantity));
        } else {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }
}
