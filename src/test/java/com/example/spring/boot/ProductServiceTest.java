package com.example.spring.boot;

import com.example.spring.boot.domain.Product;
import com.example.spring.boot.domain.User;
import com.example.spring.boot.repository.ProductRepository;
import com.example.spring.boot.service.ProductService;
import com.example.spring.boot.service.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by pc on 2016/7/19.
 */
public class ProductServiceTest {
    private ProductServiceImpl productService;
    private ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        productService = new ProductServiceImpl();
        productRepository = mockProductRepository();
        productService.setProductRepository(productRepository);
    }

    private ProductRepository mockProductRepository() {
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.save(any(Product.class))).then(new Answer<Product>() {
            @Override
            public Product answer(InvocationOnMock invocation) throws Throwable {
                Product product = invocation.getArgumentAt(0, Product.class);
                product.setId(new Random().nextLong());
                return product;
            }
        });
        return productRepository;

    }

    @Test
    public void createProductSuccess() throws Exception {
        Product product = new Product();
        product.setName("macbook pro 13");
        product.setStock(20);
        product.setPrice(13500.00);
        productService.save(product);
        assertThat(product.getId()).isNotNull();
    }
}
