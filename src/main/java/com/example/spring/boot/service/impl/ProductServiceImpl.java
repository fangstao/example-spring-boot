package com.example.spring.boot.service.impl;

import com.example.spring.boot.domain.Product;
import com.example.spring.boot.repository.ProductRepository;
import com.example.spring.boot.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by pc on 2016/7/18.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;


    @Override
    @Transactional(readOnly = false, rollbackFor = Throwable.class)
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findOne(id);
    }

    @Resource
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
