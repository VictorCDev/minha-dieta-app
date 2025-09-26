package com.minhadieta.dietaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MealItemRequest {

    @NotNull(message = "O ID do ingrediente é obrigatório.")
    private Long ingredientId;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser um valor positivo.")
    private BigDecimal quantity;

    @NotBlank(message = "A unidade de medida é obrigatória.")
    private String unit;

}
