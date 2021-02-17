package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.Optional;

public class DefaultCartService implements CartService {

    public static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    public static CartService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CartService instance = new DefaultCartService();
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
        countQuantityAndCost(cart);
    }

    @Override
    public void update(Cart cart, Product product, int quantity) throws OutOfStockException {
        Optional<CartItem> item = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getCode().equals(product.getCode()))
                .findAny();
        if (item.isPresent()) {
            if (quantity <= product.getStock()) {
                item.get().setQuantity(quantity);
            } else {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        }
        countQuantityAndCost(cart);
    }

    @Override
    public void delete(Cart cart, Product product) {
        Optional<CartItem> itemToDelete = cart.getCartItems().stream()
                .filter(item -> product.getId().equals(item.getProduct().getId()))
                .findAny();
        itemToDelete.ifPresent(cartItem -> cart.getCartItems().remove(cartItem));
        countQuantityAndCost(cart);
    }

    private void countQuantityAndCost(Cart cart) {
        cart.setTotalQuantity(cart.getCartItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(item -> item)
                .sum()
        );
        cart.setTotalCost(cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
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
