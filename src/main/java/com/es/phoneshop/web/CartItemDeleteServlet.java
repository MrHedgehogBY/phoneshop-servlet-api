package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CartItemDeleteServlet extends AbstractServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long productIdToDelete = parseProductById(request);
        handlingGetProduct(request, response, () -> {
            Product product = productDao.get(productIdToDelete);
            cartService.delete(serviceGetter.getCart(request), product);
        });
        response.sendRedirect(request.getContextPath() + "/cart"
                + "?message=Item from cart deleted successfully");
    }
}
