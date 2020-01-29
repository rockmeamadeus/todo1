package com.example.todo1.repository;

import com.example.todo1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The product's repository.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
