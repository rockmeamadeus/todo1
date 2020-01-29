package com.example.todo1.controller;

import com.example.todo1.dto.ProductDto;
import com.example.todo1.entity.Product;
import com.example.todo1.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private static int STOCK = 10;

    private static int NO_STOCK = 0;


    @Test
    public void findAll() throws Exception {
        this.mockMvc.perform(get("/products")).andDo(print()).andExpect(status().isOk());
    }


    @Test
    void create() throws Exception {

        mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ProductDto.builder().id(1L).name("name1").description("description1").stock(STOCK).build())))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {

        Product productFromDatabase = Product.builder().id(1L).build();

        when(productService.findById(productFromDatabase.getId())).thenReturn(java.util.Optional.of(productFromDatabase));

        this.mockMvc.perform(get("/products/{id}", productFromDatabase.getId())).andDo(print()).andExpect(status().isOk());

    }

    @Test
    void findByIdNotFound() throws Exception {

        Product productFromDatabase = Product.builder().id(1L).build();

        when(productService.findById(productFromDatabase.getId())).thenReturn(java.util.Optional.of(productFromDatabase));

        this.mockMvc.perform(get("/products/{id}", 2L)).andDo(print()).andExpect(status().isNotFound());

    }

    @Test
    void update() throws Exception {

        ProductDto product = ProductDto.builder().id(1L).name("name1").description("description1").stock(STOCK).build();
        Product productFromDatabase = Product.builder().id(1L).name("name1").description("description1").stock(STOCK).build();

        when(productService.findById(product.getId())).thenReturn(java.util.Optional.of(productFromDatabase));

        mockMvc.perform(
                put("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());

        verify(productService, times(1)).findById(product.getId());
        verify(productService, times(1)).save(any());
        verifyNoMoreInteractions(productService);
    }

    @Test
    void updateEntityNotFoundBadRequest() throws Exception {
        ProductDto product = ProductDto.builder().id(1L).name("name1").description("description1").stock(STOCK).build();

        mockMvc.perform(
                put("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isBadRequest());

        verify(productService, times(1)).findById(product.getId());
        verify(productService, times(0)).save(any());
        verifyNoMoreInteractions(productService);
    }

    @Test
    void updateNotEnoughStockBadRequest() throws Exception {
        ProductDto product = ProductDto.builder().id(1L).stock(-STOCK).build();
        Product productFromDatabase = Product.builder().id(1L).name("name1").description("description1").price(BigDecimal.TEN).stock(STOCK - 1).build();

        when(productService.findById(product.getId())).thenReturn(java.util.Optional.of(productFromDatabase));

        mockMvc.perform(
                put("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isBadRequest());

        verify(productService, times(1)).findById(product.getId());
        verify(productService, times(0)).save(productFromDatabase);
        verifyNoMoreInteractions(productService);
    }


    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProductFound() throws Exception {

        Product productFromDatabase = Product.builder().id(1L).stock(STOCK).build();

        when(productService.findById(productFromDatabase.getId())).thenReturn(java.util.Optional.of(productFromDatabase));

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", productFromDatabase.getId()))
                .andExpect(status().isOk());
    }


}