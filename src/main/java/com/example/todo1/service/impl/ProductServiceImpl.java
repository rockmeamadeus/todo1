package com.example.todo1.service.impl;

import com.example.todo1.entity.Product;
import com.example.todo1.repository.ProductRepository;
import com.example.todo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRespository;

    @Override
    public List<Product> findAll() {
        return productRespository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRespository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRespository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRespository.deleteById(id);
    }
}
