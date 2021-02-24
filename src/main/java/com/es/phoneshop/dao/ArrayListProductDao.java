package com.es.phoneshop.dao;

import com.es.phoneshop.model.comparator.SearchComparator;
import com.es.phoneshop.model.comparator.SortingComparator;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sortenum.SortField;
import com.es.phoneshop.model.sortenum.SortOrder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao extends AbstractDao<Product> implements ProductDao {

    public static ProductDao getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ProductDao instance = new ArrayListProductDao();
    }

    private ArrayListProductDao() {

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
            Stream<Product> productStream = items.stream()
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
    public void delete(Long id) {
        lock.safeWrite((Long tempId) -> items.remove(this.get(tempId)), id);
    }
}
