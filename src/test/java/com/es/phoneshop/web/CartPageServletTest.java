package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;


    private CartPageServlet servlet = new CartPageServlet();
    private ProductDao productDao;
    private CartService cartService;
    private ServiceGetter serviceGetter;
    private Cart cart;
    private Locale locale;
    private Product testProduct = new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");

    public CartPageServletTest() {
    }

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        serviceGetter = ServiceGetter.getInstance();
        locale = new Locale("russian");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        cart = serviceGetter.getCart(request);
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
        verify(request).setAttribute(eq("cart"), any());
    }

    @Test
    public void testDoPost() throws IOException, ServletException, OutOfStockException {
        productDao.save(testProduct);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        cartService.add(serviceGetter.getCart(request), testProduct, 1);
        when(request.getParameterValues("productId")).thenReturn(new String[]{testProduct.getId().toString()});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"5"});
        when(request.getLocale()).thenReturn(locale);
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test(expected = NumberFormatException.class)
    public void testDoPostIncorrectId() throws ServletException, IOException, OutOfStockException {
        productDao.save(testProduct);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        cartService.add(serviceGetter.getCart(request), testProduct, 1);
        when(request.getParameterValues("productId")).thenReturn(new String[]{"e"});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});
        servlet.doPost(request, response);
    }

    @Test
    public void testDoPostParseException() throws OutOfStockException, ServletException, IOException {
        productDao.save(testProduct);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        cartService.add(serviceGetter.getCart(request), testProduct, 1);
        when(request.getParameterValues("productId")).thenReturn(new String[]{testProduct.getId().toString()});
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"e"});
        when(request.getLocale()).thenReturn(locale);
        servlet.doPost(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}
