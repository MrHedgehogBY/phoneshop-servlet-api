package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemDeleteServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    private CartItemDeleteServlet servlet = new CartItemDeleteServlet();
    private Product testProduct = new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private ProductDao productDao;
    private ServiceGetter serviceGetter;
    private CartService cartService;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        productDao = ArrayListProductDao.getInstance();
        serviceGetter = ServiceGetter.getInstance();
        cartService = DefaultCartService.getInstance();
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getSession()).thenReturn(session);
    }

    @After
    public void tearDown() {
        productDao.clear();
    }

    @Test
    public void testDoPost() throws IOException {
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostDelete() throws OutOfStockException, IOException {
        productDao.save(testProduct);
        cartService.add(serviceGetter.getCart(request), productDao.get(0L), 1);
        servlet.doPost(request, response);
        assertTrue(serviceGetter.getCart(request).getCartItems().isEmpty());
    }
}
