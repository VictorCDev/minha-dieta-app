package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {

    // Método para encontrar um item específico dentro de uma refeição específica.
    // Será muito útil para as operações de UPDATE e DELETE.
    Optional<MealItem> findByIdAndMealConfiguration_Id(Long id, Long mealConfigurationId);

}
