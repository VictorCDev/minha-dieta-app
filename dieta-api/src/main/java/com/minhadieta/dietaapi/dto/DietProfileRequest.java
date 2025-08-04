package com.minhadieta.dietaapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietProfileRequest {

    @NotBlank(message = "O nome do perfil não pode estar em branco.")
    @Size(max = 100, message = "O nome do perfil deve ter no máximo 100 caracteres.")
    private String profileName;

    @Size(max = 255, message = "O objetivo deve ter no máximo 255 caracteres.")
    private String goal;

    // Não incluir 'isActive' aqui, pois o status ativo será gerenciado por um endpoint separado
    // e o usuário ao criar, não define isso diretamente.
}
