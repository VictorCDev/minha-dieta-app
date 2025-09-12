package com.minhadieta.dietaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {

    @NotBlank(message = "O nome do ingrediente é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do ingrediente deve ter entre 2 e 100 caracteres.")
    private String name;
}
