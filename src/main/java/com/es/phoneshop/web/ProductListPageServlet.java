package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private ServiceGetter serviceGetter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        serviceGetter = new ServiceGetter();
    }

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
        request.setAttribute("history",
                serviceGetter.getRecentHistory(request).getRecentProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
