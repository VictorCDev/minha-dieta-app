package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.MealConfiguration;
import com.minhadieta.dietaapi.model.DietProfile; // Importação necessária
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealConfigurationRepository extends JpaRepository<MealConfiguration, Long> {

    // através do DietProfile, ordenadas pela mealOrder (nome correto da propriedade)
    List<MealConfiguration> findByDietProfile_User_IdOrderByMealOrderAsc(Long userId);

    // e pelo usuário (através do DietProfile)
    Optional<MealConfiguration> findByMealNameAndDietProfile_User_Id(String mealName, Long userId);

    // Encontra todas as configurações de refeição para um perfil de dieta específico
    List<MealConfiguration> findByDietProfile(DietProfile dietProfile);

    // Encontra uma configuração de refeição por mealName e o DietProfile específico
    Optional<MealConfiguration> findByMealNameAndDietProfile(String mealName, DietProfile dietProfile);

    // Encontra uma configuração de refeição por ID e o DietProfile específico
    Optional<MealConfiguration> findByIdAndDietProfile(Long id, DietProfile dietProfile);

    // Encontra uma configuração de refeição por ID e o DietProfile do usuário
    Optional<MealConfiguration> findByIdAndDietProfile_User_Id(Long id, Long userId);

    // Busca uma MealConfiguration pelo seu ID, pelo ID do seu DietProfile e pelo ID do User do DietProfile.
    // Isso garante a posse completa da hierarquia.
    Optional<MealConfiguration> findByIdAndDietProfile_IdAndDietProfile_User_Id(Long mealId, Long dietProfileId, Long userId);
}
