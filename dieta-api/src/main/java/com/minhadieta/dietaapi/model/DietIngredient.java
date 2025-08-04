package com.minhadieta.dietaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diet_ingredient")
public class DietIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Muitos-para-Um com DietaSemanal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_diet_id  ", nullable = false)
    private WeeklyDiet weeklyDiet;

    // Relacionamento Muitos-para-Um com Ingrediente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "quantity", nullable = false)
    private Double quantity; // Quantidade do ingrediente (sempre em gramas)
}
