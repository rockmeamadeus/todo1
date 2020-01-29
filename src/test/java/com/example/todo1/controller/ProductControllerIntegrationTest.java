package com.example.todo1.controller;

import com.example.todo1.dto.CreateRequestDto;
import com.example.todo1.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static int STOCK = 10;


    @Test
    void integrationTestSaveUpdateDeleteOk() throws Exception {

        CreateRequestDto productToUpdate = CreateRequestDto.builder().name("name1").price(BigDecimal.TEN).stock(STOCK).description("description1").build();

        ProductDto product = ProductDto.builder().name("name2").price(BigDecimal.ONE).stock(STOCK + 10).description("description2").build();

        MvcResult mvcResult = this.mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productToUpdate)))
                .andExpect(status().isOk()).andReturn();


        JSONObject jsonObj = new JSONObject(mvcResult.getResponse().getContentAsString());

        String productId = jsonObj.getString("id");


        this.mockMvc.perform(
                put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/products/{id}", productId))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("1.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock").value("30"));

        this.mockMvc.perform(
                delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk());

        this.mockMvc.perform(
                delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isNotFound());


    }
}