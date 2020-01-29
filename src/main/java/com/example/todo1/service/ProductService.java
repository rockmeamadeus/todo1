package com.example.todo1.service;

import com.example.todo1.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);
}
