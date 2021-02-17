package com.es.phoneshop.dao;

import com.es.phoneshop.model.comparator.SearchComparator;
import com.es.phoneshop.model.comparator.SortingComparator;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;
import com.es.phoneshop.model.threadsafe.ExtendedReadWriteLock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {

    private List<Product> products;
    private long lastId;
    private ExtendedReadWriteLock lock = new ExtendedReadWriteLock();

    public static ProductDao getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ProductDao instance = new ArrayListProductDao();
    }

    private ArrayListProductDao() {
        lastId = 0L;
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) {
        return lock.safeRead(() -> products.stream()
                .filter(product -> product.getId().equals(id))
                .findAny()
                .orElseThrow(NoSuchElementException::new)
        );
    }

    private boolean containsWord(Product product, List<String> wordsList) {
        return wordsList.stream().anyMatch(word -> product.getDescription().contains(word));
    }

    @Override
    public List<Product> findProducts() {
        return findProducts(null, null, null);
    }

    @Override
    public List<Product> findProducts(String search, SortField sortField, SortOrder sortOrder) {
        return lock.safeRead(() -> {
            Stream<Product> productStream = products.stream()
                    .filter(product -> search == null || search.isEmpty()
                            || containsWord(product, Arrays.asList(search.split(" "))))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0);
            if (search != null) {
                List<String> wordsList = Arrays.asList(search.split(" "));
                productStream = productStream.sorted(new SearchComparator(wordsList).reversed());
            }
            if (sortField != null && sortOrder != null) {
                productStream = productStream.sorted(new SortingComparator(sortField, sortOrder));
            }
            return productStream.collect(Collectors.toList());
        });
    }

    @Override
    public void save(Product product) {
        lock.safeWrite((Product item) -> {
                    if (item.getId() != null) {
                        Product sameIdProduct = getProduct(item.getId());
                        products.remove(sameIdProduct);
                    } else {
                        item.setId(lastId++);
                    }
                    products.add(item);
                },
                product
        );
    }

    @Override
    public void delete(Long id) {
        lock.safeWrite((Long tempId) -> products.remove(getProduct(tempId)), id);
    }

    @Override
    public void clear() {
        lock.safeWrite(() -> {
            products.clear();
            lastId = 0;
        });
    }
}
