package com.example.todo1.controller;

import com.example.todo1.dto.CreateRequestDto;
import com.example.todo1.dto.ProductDto;
import com.example.todo1.entity.Product;
import com.example.todo1.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CreateRequestDto productDto) {
        return ResponseEntity.ok(productService.save(new ModelMapper().map(productDto, Product.class)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return this.productService.findById(id)
                .map(c -> ResponseEntity.ok(c))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {

        return this.productService.findById(id)
                .filter(product1 -> product1.getStock() + productDto.getStock() > 0)
                .map(product1 -> {

                    if (StringUtils.isNotBlank(productDto.getName())) {
                        product1.setName(productDto.getName());
                    }

                    if (StringUtils.isNotBlank(productDto.getDescription())) {
                        product1.setDescription(productDto.getDescription());
                    }

                    if (productDto.getPrice() != null) {
                        product1.setPrice(productDto.getPrice());
                    }

                    product1.setStock(product1.getStock() + productDto.getStock());

                    return ResponseEntity.ok(productService.save(product1));
                })
                .orElse(ResponseEntity.badRequest().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return this.productService.findById(id)
                .map(product -> {
                    log.info("deleting product id: " + id);
                    productService.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());

    }

}
