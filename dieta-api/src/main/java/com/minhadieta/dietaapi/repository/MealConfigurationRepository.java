package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.MealConfiguration;
import com.minhadieta.dietaapi.model.DietProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealConfigurationRepository extends JpaRepository<MealConfiguration, Long> {

    // MÉTODO ESSENCIAL 1: Usado no Service para buscar uma refeição específica
    // garantindo que ela pertence ao perfil de dieta correto. É a chave para a
    // segurança nas operações de GET (por id), UPDATE e DELETE.
    Optional<MealConfiguration> findByIdAndDietProfile_Id(Long id, Long dietProfileId);

    // MÉTODO ESSENCIAL 2: Usado para buscar todas as refeições de um perfil.
    // Embora o Service consiga essa lista através da entidade DietProfile, este
    // método é uma alternativa direta e eficiente.
    List<MealConfiguration> findByDietProfile(DietProfile dietProfile);

    /*
     * SEUS OUTROS MÉTODOS (opcionais, mas muito bons):
     *
     * findByDietProfile_User_IdOrderByMealOrderAsc(Long userId);
     * -> Ótimo para buscar todas as refeições de um usuário de uma vez, já ordenadas.
     *
     * findByMealNameAndDietProfile_User_Id(String mealName, Long userId);
     * -> Ótimo para validações complexas, como garantir que o nome de uma refeição
     * é único para todo um usuário, e não apenas para um perfil.
     */

}