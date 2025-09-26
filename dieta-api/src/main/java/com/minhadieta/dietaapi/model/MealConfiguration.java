package com.minhadieta.dietaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meal_configuration")
public class MealConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meal_order", nullable = false)
    private Integer mealOrder; // Ex: 1 para café da manhã, 2 para lanche, etc.

    @Column(name = "meal_name", nullable = false, length = 100)
    private String mealName; // Ex: "Café da Manhã", "Almoço", "Lanche da Tarde"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_profile_id", nullable = false) // Coluna da chave estrangeira
    private DietProfile dietProfile; // O perfil de dieta ao qual esta configuração de refeição pertence

    @OneToMany(mappedBy = "mealConfiguration", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MealItem> mealItems; // Lista dos ingredientes/itens desta refeição

}
