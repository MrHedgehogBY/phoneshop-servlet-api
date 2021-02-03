package com.es.phoneshop.model.product;

import com.es.phoneshop.model.recent.DefaultRecentHistoryService;
import com.es.phoneshop.model.recent.RecentHistory;
import com.es.phoneshop.model.recent.RecentHistoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class DefaultRecentHistoryServiceTest {

    private RecentHistoryService historyService = DefaultRecentHistoryService.getInstance();
    private RecentHistory recentHistory = new RecentHistory();
    private Product firstProduct = new Product(1L, "test-product", "Samsung Galaxy S II", new BigDecimal(500), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private Product secondProduct = new Product(2L, "palmp", "Palm Pixi", new BigDecimal(170), Currency.getInstance("USD"), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg");
    private Product thirdProduct = new Product(3L, "3rd", "Samsung Galaxy S II", new BigDecimal(500), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private Product fourthProduct = new Product(4L, "4th", "Samsung Galaxy S II", new BigDecimal(500), Currency.getInstance("USD"), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");


    @Before
    public void setup() {
        recentHistory.getRecentProducts().add(firstProduct);
    }

    @After
    public void tearDown() {
        recentHistory.getRecentProducts().clear();
    }

    @Test
    public void testAddSameItemToHistory() {
        int size = recentHistory.getRecentProducts().size();
        historyService.add(recentHistory, firstProduct);
        assertEquals(size, recentHistory.getRecentProducts().size());
    }

    @Test
    public void testAddNewItemToHistory() {
        int size = recentHistory.getRecentProducts().size();
        historyService.add(recentHistory, secondProduct);
        assertEquals(size, recentHistory.getRecentProducts().size() - 1);
    }

    @Test
    public void testAddFirstOneAgain() {
        historyService.add(recentHistory, secondProduct);
        historyService.add(recentHistory, firstProduct);
        assertEquals(firstProduct, recentHistory.getRecentProducts().peekFirst());
    }

    @Test
    public void testAddAfterThird() {
        historyService.add(recentHistory, secondProduct);
        historyService.add(recentHistory, thirdProduct);
        int size = recentHistory.getRecentProducts().size();
        historyService.add(recentHistory, fourthProduct);
        assertEquals(size, recentHistory.getRecentProducts().size());
    }
}
