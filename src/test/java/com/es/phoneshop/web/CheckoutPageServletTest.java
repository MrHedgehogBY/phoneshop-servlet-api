package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
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

    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private ServiceGetter serviceGetter;
    private CartService cartService;
    private Cart cart;
    private int quantity = 2;
    private Product firstProduct = new Product(1L, "test-product", "Samsung Galaxy S II", new BigDecimal(500), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");


    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        serviceGetter = ServiceGetter.getInstance();
        cartService = DefaultCartService.getInstance();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        cart = serviceGetter.getCart(request);
    }

    @Test
    public void testDoGetWithEmptyCart() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoGetWithNotEmptyCart() throws ServletException, IOException, OutOfStockException {
        cartService.add(cart, firstProduct, quantity);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithNotEmptyCartAttributes() throws ServletException, IOException, OutOfStockException {
        cartService.add(cart, firstProduct, quantity);
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
    }

    @Test
    public void testDoPostErrors() throws ServletException, IOException {
        cart.setTotalCost(firstProduct.getPrice());
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        when(request.getParameter(anyString())).thenReturn("");
        servlet.doPost(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostErrorsAttributes() throws ServletException, IOException {
        cart.setTotalCost(firstProduct.getPrice());
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        when(request.getParameter(anyString())).thenReturn("");
        servlet.doPost(request, response);
        verify(request).setAttribute(eq("errors"), any());
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
    }

    @Test
    public void testDoPostSuccessful() throws ServletException, IOException {
        when(request.getParameter(eq("firstName"))).thenReturn("eugene");
        when(request.getParameter(eq("lastName"))).thenReturn("bylkov");
        when(request.getParameter(eq("phone"))).thenReturn("+375291111111");
        when(request.getParameter(eq("deliveryDate"))).thenReturn("01-01-2020");
        when(request.getParameter(eq("deliveryAddress"))).thenReturn("address");
        when(request.getParameter(eq("paymentMethod"))).thenReturn("CACHE");
        cart.setTotalCost(firstProduct.getPrice());
        when(request.getSession().getAttribute(anyString())).thenReturn(cart);
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }
}
