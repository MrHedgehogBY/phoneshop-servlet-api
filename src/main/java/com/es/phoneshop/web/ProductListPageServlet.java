package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;


public class ProductListPageServlet extends AbstractServlet {

    private String listPage = "/WEB-INF/pages/productList.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        try {
            request.setAttribute("products", productDao.findProducts(search,
                    Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                    Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
            ));
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error");
            response.sendError(500);
        }
        historyToPdpPage(request, response, listPage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = Long.parseLong(request.getParameter("productId"));
        try {
            addItemToCart(request, productId);
        } catch (OutOfStockException | ParseException e) {
            String message = e.getClass() == OutOfStockException.class ? "Out of stock" : "Not a number";
            request.setAttribute("error", message);
            request.setAttribute("errorId", String.valueOf(productId));
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath()
                + request.getServletPath() + "?message=Added to cart successfully");
    }
}
