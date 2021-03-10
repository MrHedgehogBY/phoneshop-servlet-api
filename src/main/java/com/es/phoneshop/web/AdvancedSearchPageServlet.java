package com.es.phoneshop.web;

import com.es.phoneshop.search.SearchMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;


public class AdvancedSearchPageServlet extends AbstractServlet {

    private String listPage = "/WEB-INF/pages/advancedSearch.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String searchMethod = request.getParameter("searchMethod");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        HashMap<String, String> errors = new HashMap<>();
        if (minPrice != null && !minPrice.isEmpty()) {
            checkPriceParsing(minPrice, "MinPrice", errors, request, response);
        }
        if (maxPrice != null && !maxPrice.isEmpty()) {
            checkPriceParsing(maxPrice, "MaxPrice", errors, request, response);
        }
        if (!errors.isEmpty()) {
            request.setAttribute("searchMethods", Arrays.asList(SearchMethod.values()));
            request.setAttribute("products", productDao.findProducts());
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(listPage).forward(request, response);
        }
        if (search == null || search.isEmpty()) {
            request.setAttribute("products", productDao.findProducts());
        } else {
            BigDecimal minPriceValue = parsePrice(minPrice, "MinPrice");
            BigDecimal maxPriceValue = parsePrice(maxPrice, "MaxPrice");
            request.setAttribute("products", productDao.advancedSearch(search, searchMethod, minPriceValue, maxPriceValue));
        }
        request.setAttribute("searchMethods", Arrays.asList(SearchMethod.values()));
        request.getRequestDispatcher(listPage).forward(request, response);
    }

    private void checkPriceParsing(String price, String parameter, HashMap<String, String> errors,
                                   HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            BigDecimal priceValue = BigDecimal.valueOf(Double.parseDouble(price));
        } catch (NumberFormatException e) {
            errors.put("error" + parameter, "Not a number");
        }
    }

    private BigDecimal parsePrice(String price, String parameter) {
        if (price == null || price.isEmpty()) {
            if (parameter.equals("MinPrice")) {
                return BigDecimal.valueOf(Double.MIN_VALUE);
            } else {
                return BigDecimal.valueOf(Double.MAX_VALUE);
            }
        } else {
            return BigDecimal.valueOf(Double.parseDouble(price));
        }
    }
}
