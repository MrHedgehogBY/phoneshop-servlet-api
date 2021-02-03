package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.recent.DefaultRecentHistoryService;
import com.es.phoneshop.model.recent.RecentHistoryService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;
    private RecentHistoryService historyService;
    private ServiceGetter serviceGetter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        historyService = DefaultRecentHistoryService.getInstance();
        serviceGetter = new ServiceGetter();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            setProductAttributes(request);
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
        historyService.add(serviceGetter.getRecentHistory(request),
                productDao.getProduct(parseProductById(request)));
        historyToPdpPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId;
        int quantity;
        try {
            productId = parseProductById(request);
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(request.getParameter("quantity")).intValue();
            Cart cart = serviceGetter.getCart(request);
            cartService.add(cart, productDao.getProduct(parseProductById(request)), quantity);
        } catch (OutOfStockException | ParseException e) {
            String message = e.getClass() == OutOfStockException.class ? "Out of stock" : "Not a number";
            request.setAttribute("error", message);
            setProductAttributes(request);
            historyToPdpPage(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + request.getServletPath() +
                "/" + productId + "?message=Added to cart successfully");
    }

    private long parseProductById(HttpServletRequest request) {
        String idString = request.getPathInfo().substring(1);
        return Long.parseLong(idString);
    }

    private void setProductAttributes(HttpServletRequest request) {
        request.setAttribute("product", productDao.getProduct(parseProductById(request)));
        request.setAttribute("cart", serviceGetter.getCart(request));
    }

    private void historyToPdpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("history",
                serviceGetter.getRecentHistory(request).getRecentProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }
}
