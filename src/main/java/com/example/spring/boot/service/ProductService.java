package com.example.spring.boot.service;

import com.example.spring.boot.domain.Product;

/**
 * Created by fangtao on 16/7/10.
 */
public interface ProductService {

    Product save(Product product);

    Product findById(Long id);
}
