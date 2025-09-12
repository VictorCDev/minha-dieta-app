package com.minhadieta.dietaapi.dto;

import lombok.Data;

@Data
public class MealConfigurationResponse {

    private Long id;
    private Integer mealOrder;
    private String mealName;
    private Long dietProfileId;
}
