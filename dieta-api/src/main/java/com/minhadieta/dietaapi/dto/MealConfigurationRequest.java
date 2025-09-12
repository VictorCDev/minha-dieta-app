package com.minhadieta.dietaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MealConfigurationRequest {

    @NotNull(message = "A ordem da refeição é obrigatória.")
    @Positive(message = "A ordem da refeição deve ser um número positivo.")
    private Integer mealOrder;

    @NotBlank(message = "O nome da refeição é obrigatório.")
    private String mealName;
}
