package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private Product testProduct = new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");


    @Before
    public void setup() throws ServletException {
        productDao.save(testProduct);
        servlet.init(config);
        when(request.getPathInfo()).thenReturn("/" + testProduct.getId());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @After
    public void tearDown() {
        productDao.clear();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetCheckSetAttribute() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("product"), any());
    }

    @Test
    public void testDoGetIncorrectId() throws ServletException, IOException{
        when(request.getPathInfo()).thenReturn("/" + testProduct.getCode());
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("error"), eq("Incorrect id format"));
    }

    @Test
    public void testDoGetIllegalId() throws ServletException, IOException{
        when(request.getPathInfo()).thenReturn("/" + testProduct.getStock());
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("error"), eq("Illegal product id, no such element"));
    }

}
