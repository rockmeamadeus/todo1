package com.example.todo1.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Builder
public class CreateRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    private BigDecimal price;

    @Min(1)
    private int stock;
}
