package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo().substring(1);
        try {
            request.setAttribute("product", productDao.getProduct(Long.valueOf(productId)));
        } catch (NoSuchElementException e) {
            request.setAttribute("error", "Illegal product id, no such element");
            response.sendError(404);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Incorrect id format");
            response.sendError(404);
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error");
            response.sendError(500);
        }
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }
}
