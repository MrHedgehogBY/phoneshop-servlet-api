package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.recent.DefaultRecentHistoryService;
import com.es.phoneshop.model.recent.RecentHistoryService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class ProductDetailsPageServlet extends AbstractServlet {

    private RecentHistoryService historyService;
    private String detailsPage = "/WEB-INF/pages/productDetails.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        historyService = DefaultRecentHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handlingGetProduct(request, response, () -> setProductAttributes(request));
        historyService.add(serviceGetter.getRecentHistory(request),
                productDao.get(parseProductById(request)));
        historyToPdpPage(request, response, detailsPage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId = parseProductById(request);
        try {
            addItemToCart(request, productId);
        } catch (OutOfStockException | ParseException e) {
            String message = e.getClass() == OutOfStockException.class ? "Out of stock" : "Not a number";
            request.setAttribute("error", message);
            setProductAttributes(request);
            historyToPdpPage(request, response, detailsPage);
            return;
        }
        response.sendRedirect(request.getContextPath() + request.getServletPath() +
                "/" + productId + "?message=Added to cart successfully");
    }

    private void setProductAttributes(HttpServletRequest request) {
        request.setAttribute("product", productDao.get(parseProductById(request)));
        request.setAttribute("cart", serviceGetter.getCart(request));
    }
}
