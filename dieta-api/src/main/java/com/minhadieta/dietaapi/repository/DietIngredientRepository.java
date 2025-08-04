package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.DietIngredient;
import com.minhadieta.dietaapi.model.WeeklyDiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietIngredientRepository extends JpaRepository<DietIngredient, Long> {

    // Retorna todos os ingredientes associados a uma entrada de DietaSemanal específica
    // Seguindo a preferência de nomenclatura em inglês
    List<DietIngredient> findByWeeklyDiet(WeeklyDiet weeklyDiet);

    // Retorna todos os ingredientes para um ID de dieta semanal específico
    List<DietIngredient> findByWeeklyDietId(Long weeklyDietId);
}
