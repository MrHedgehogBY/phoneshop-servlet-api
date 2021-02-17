package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.threadsafe.Volunteer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;

public abstract class AbstractServlet extends HttpServlet {

    protected CartService cartService;
    protected ProductDao productDao;
    protected ServiceGetter serviceGetter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        serviceGetter = ServiceGetter.getInstance();
    }

    protected void historyToPdpPage(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        request.setAttribute("history",
                serviceGetter.getRecentHistory(request).getRecentProducts());
        request.getRequestDispatcher(page).forward(request, response);
    }

    protected long parseProductById(HttpServletRequest request) {
        String idString = request.getPathInfo().substring(1);
        return Long.parseLong(idString);
    }

    protected void handlingGetProduct(HttpServletRequest request, HttpServletResponse response, Volunteer function)
            throws IOException {
        try {
            function.perform();
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
    }

    protected int quantityParser(HttpServletRequest request, String quantity) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantity).intValue();
    }

    protected void addItemToCart(HttpServletRequest request, long productId) throws ParseException, OutOfStockException {
        int quantity = quantityParser(request, request.getParameter("quantity"));
        cartService.add(serviceGetter.getCart(request), productDao.getProduct(productId), quantity);
    }

}
