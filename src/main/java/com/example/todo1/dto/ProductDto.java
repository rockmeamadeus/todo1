package com.example.todo1.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {


    private Long id;

    private String name;

    private String description;

    @Min(1)
    private BigDecimal price;

    private int stock;

}

