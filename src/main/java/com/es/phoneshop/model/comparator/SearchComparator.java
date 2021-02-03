package com.es.phoneshop.model.comparator;

import com.es.phoneshop.model.product.Product;

import java.util.Comparator;
import java.util.List;

public class SearchComparator implements Comparator<Product> {

    private List<String> wordsList;

    public SearchComparator(List<String> words) {
        wordsList = words;
    }

    @Override
    public int compare(Product product1, Product product2) {
        long wordsInProduct1 = wordsList.stream()
                .filter(word -> product1.getDescription().contains(word))
                .count();
        long wordsInProduct2 = wordsList.stream()
                .filter(word -> product2.getDescription().contains(word))
                .count();
        Double wordsPercentageProduct1 = wordsInProduct1 / (double) product1.getDescription().split(" ").length;
        Double wordsPercentageProduct2 = wordsInProduct2 / (double) product2.getDescription().split(" ").length;
        return wordsPercentageProduct1.compareTo(wordsPercentageProduct2);
    }
}
