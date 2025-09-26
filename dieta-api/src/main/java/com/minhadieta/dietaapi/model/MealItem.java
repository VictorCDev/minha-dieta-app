package com.minhadieta.dietaapi.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meal_item")
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: Muitos MealItems pertencem a UMA MealConfiguration.
    // Esta é a refeição à qual este item pertence (ex: Café da Manhã).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_configuration_id", nullable = false)
    private MealConfiguration mealConfiguration;

    // Relacionamento: Muitos MealItems podem usar UM Ingredient.
    // Este é o ingrediente deste item (ex: Ovo).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // Usamos BigDecimal para precisão com números decimais

    @Column(nullable = false, length = 50)
    private String unit; // Ex: "gramas", "ml", "unidades"
}
