package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.recent.DefaultRecentHistoryService;
import com.es.phoneshop.model.recent.RecentHistory;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

public class ServiceGetter {

    public RecentHistory getRecentHistory(HttpServletRequest request) {
        RecentHistory recentHistory;
        Object mutex = WebUtils.getSessionMutex(request.getSession());
        synchronized (mutex) {
            Object attribute = request.getSession().getAttribute(DefaultRecentHistoryService.HISTORY_SESSION_ATTRIBUTE);
            recentHistory = getService(attribute, RecentHistory.class);
            if (attribute == null) {
                request.getSession().setAttribute(DefaultRecentHistoryService.HISTORY_SESSION_ATTRIBUTE, recentHistory);
            }
        }
        return recentHistory;
    }

    public Cart getCart(HttpServletRequest request) {
        Cart cart;
        Object mutex = WebUtils.getSessionMutex(request.getSession());
        synchronized (mutex) {
            Object attribute = request.getSession().getAttribute(DefaultCartService.CART_SESSION_ATTRIBUTE);
            cart = getService(attribute, Cart.class);
            if (attribute == null) {
                request.getSession().setAttribute(DefaultCartService.CART_SESSION_ATTRIBUTE, cart);
            }
        }
        return cart;
    }

    public <T> T getService(Object attribute, Class<T> tClass) {
        T service = (T) attribute;
        if (service == null) {
            try {
                service = tClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return service;
    }
}
