package com.es.phoneshop.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPricesPageServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handlingGetProduct(request, response,
                () -> request.setAttribute("product", productDao.get(parseProductById(request))));
        request.getRequestDispatcher("/WEB-INF/pages/productPrices.jsp").forward(request, response);
    }
}
