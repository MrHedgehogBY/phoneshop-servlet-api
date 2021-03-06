package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {

    private ProductDao productDao = ArrayListProductDao.getInstance();
    private Currency usd = Currency.getInstance("USD");

    private Product updatedProduct = new Product(1L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private Product newProduct = new Product("test-product", "Samsung Galaxy S II", new BigDecimal(500), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private Product zeroStockProduct = new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
    private Product nullPriceProduct = new Product("sgs2", "Samsung Galaxy S II", null, usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");

    @Before
    public void setup() {
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
    }

    @After
    public void tearDown() {
        productDao.clear();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteProduct() {
        productDao.delete(1L);
        productDao.get(1L);
    }

    @Test
    public void testSaveNewProduct() {
        productDao.save(newProduct);
        Product result = productDao.get(newProduct.getId());
        assertEquals(newProduct.getCode(), result.getCode());
    }

    @Test
    public void testSaveExistingProduct() {
        Product existingProduct = productDao.get(updatedProduct.getId());
        productDao.save(updatedProduct);
        assertNotEquals(existingProduct.getStock(), updatedProduct.getStock());
    }

    @Test
    public void testFindProductWithZeroStock() {
        productDao.save(zeroStockProduct);
        List<Product> list = productDao.findProducts();
        assertFalse(list.contains(zeroStockProduct));
    }

    @Test
    public void testFindProductWithNullPrice() {
        productDao.save(nullPriceProduct);
        List<Product> list = productDao.findProducts();
        assertFalse(list.contains(nullPriceProduct));
    }

    @Test
    public void testFindProductsSearchAndSorting() {
        productDao.save(newProduct);
        List<Product> list = productDao.findProducts("Samsung", SortField.price, SortOrder.desc);
        assertEquals(newProduct.getPrice(), list.get(0).getPrice());
    }
}
