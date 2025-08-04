package com.minhadieta.dietaapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weekly_diet")
public class WeeklyDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING) // Armazena o enum como String no BD (ex: "MONDAY")
    private DayOfWeek dayOfWeek;

    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate; // Ex: A data da segunda-feira daquela semana

    @Column(name = "total_calories", nullable = false)
    private Double totalCalories;

    @Column(name = "total_proteins", nullable = false)
    private Double totalProteins;

    @Column(name = "total_carbohydrates", nullable = false)
    private Double totalCarbohydrates;

    @Column(name = "total_fats", nullable = false)
    private Double totalFats;

    // NOVO RELACIONAMENTO: Muitos-para-Um com PerfilDieta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_profile_id", nullable = false) // Coluna da chave estrangeira
    private DietProfile dietProfile; // O perfil de dieta ao qual esta dieta semanal pertence

    // Relacionamento Um-para-Muitos com DietaIngrediente (para listar os ingredientes da dieta em um dia específico)
    // MappedBy indica que o lado DietaIngrediente é o proprietário do relacionamento
    @OneToMany(mappedBy = "weeklyDiet", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DietIngredient> dietIngredients; // Lista de ingredientes para este dia da dieta

}
