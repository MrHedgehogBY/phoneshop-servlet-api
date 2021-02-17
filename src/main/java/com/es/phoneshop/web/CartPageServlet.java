package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = serviceGetter.getCart(request);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Cart cart = serviceGetter.getCart(request);
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Product product = productDao.getProduct(Long.parseLong(productIds[i]));
            try {
                cartService.update(cart, product, quantityParser(request, quantities[i]));
            } catch (ParseException | OutOfStockException e) {
                String message = e.getClass() == OutOfStockException.class ? "Out of stock" : "Not a number";
                errors.put(Long.parseLong(productIds[i]), message);
            }
        }
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + request.getServletPath()
                    + "?message=Cart updated successfully");
        }
    }
}
