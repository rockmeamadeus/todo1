package com.example.todo1.service.impl;

import com.example.todo1.entity.Product;
import com.example.todo1.repository.ProductRepository;
import com.example.todo1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private static int STOCK = 10;

    @BeforeEach
    void init() {
        productRepository.save(Product.builder().id(1L).name("name1").description("description1").stock(STOCK).build());
    }


    @Test
    void findAll() {

        List<Product> products = productService.findAll();
        assertTrue(products.size() == 1);
    }

    @Test
    void findById() {
        Optional<Product> product = productService.findById(1L);
        assertTrue(product.isPresent());
    }


    @Test
    void deleteById() {

        productService.deleteById(1L);
        Optional<Product> product = productService.findById(1L);

        assertFalse(product.isPresent());
    }
}