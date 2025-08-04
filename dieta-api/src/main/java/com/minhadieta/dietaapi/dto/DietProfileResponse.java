package com.minhadieta.dietaapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietProfileResponse {
    private Long id;
    private String profileName;
    private String goal;
    private Long userId; // ID do usuário proprietário
    private Boolean isActive; // Status ativo do perfil
}
