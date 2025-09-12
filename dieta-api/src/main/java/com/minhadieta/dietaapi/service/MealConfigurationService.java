package com.minhadieta.dietaapi.service;

import com.minhadieta.dietaapi.dto.MealConfigurationRequest;
import com.minhadieta.dietaapi.dto.MealConfigurationResponse;
import com.minhadieta.dietaapi.model.DietProfile;
import com.minhadieta.dietaapi.model.MealConfiguration;
import com.minhadieta.dietaapi.repository.DietProfileRepository;
import com.minhadieta.dietaapi.repository.MealConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealConfigurationService {

    @Autowired
    private MealConfigurationRepository mealConfigurationRepository;
    @Autowired
    private DietProfileRepository dietProfileRepository;

    @Transactional
    public MealConfigurationResponse create(Long userId, Long dietProfileId, MealConfigurationRequest request) {
        // Busca o perfil, garantindo que ele pertence ao usuário.
        DietProfile dietProfile = findProfileForUser(userId, dietProfileId);

        // Opcional: Validar se já existe uma refeição com o mesmo nome ou ordem neste perfil
        if (dietProfile.getMealConfigurations().stream().anyMatch(m -> m.getMealName().equalsIgnoreCase(request.getMealName()))) {
            throw new IllegalArgumentException("Já existe uma refeição com este nome no perfil.");
        }

        MealConfiguration newMeal = new MealConfiguration();
        newMeal.setMealOrder(request.getMealOrder());
        newMeal.setMealName(request.getMealName());
        newMeal.setDietProfile(dietProfile);

        MealConfiguration savedMeal = mealConfigurationRepository.save(newMeal);
        return convertToResponse(savedMeal);
    }

    @Transactional(readOnly = true)
    public List<MealConfigurationResponse> findByDietProfile(Long userId, Long dietProfileId) {
        DietProfile dietProfile = findProfileForUser(userId, dietProfileId);
        // Usa a lista da entidade, que já está filtrada para o perfil correto
        return dietProfile.getMealConfigurations().stream()
                .sorted(Comparator.comparing(MealConfiguration::getMealOrder)) // Ordena pela ordem
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // MÉTODO NOVO
    @Transactional(readOnly = true)
    public MealConfigurationResponse findById(Long userId, Long dietProfileId, Long mealId) {
        findProfileForUser(userId, dietProfileId); // Valida a posse do perfil
        MealConfiguration meal = mealConfigurationRepository.findByIdAndDietProfile_Id(mealId, dietProfileId)
                .orElseThrow(() -> new IllegalArgumentException("Refeição não encontrada neste perfil."));
        return convertToResponse(meal);
    }

    // MÉTODO NOVO
    @Transactional
    public MealConfigurationResponse update(Long userId, Long dietProfileId, Long mealId, MealConfigurationRequest request) {
        findProfileForUser(userId, dietProfileId); // Valida a posse
        MealConfiguration meal = mealConfigurationRepository.findByIdAndDietProfile_Id(mealId, dietProfileId)
                .orElseThrow(() -> new IllegalArgumentException("Refeição não encontrada para atualização."));

        meal.setMealOrder(request.getMealOrder());
        meal.setMealName(request.getMealName());

        MealConfiguration updatedMeal = mealConfigurationRepository.save(meal);
        return convertToResponse(updatedMeal);
    }

    // MÉTODO NOVO
    @Transactional
    public void delete(Long userId, Long dietProfileId, Long mealId) {
        findProfileForUser(userId, dietProfileId); // Valida a posse
        if (!mealConfigurationRepository.existsById(mealId)) {
            throw new IllegalArgumentException("Refeição não encontrada com o ID: " + mealId);
        }
        mealConfigurationRepository.deleteById(mealId);
    }

    // Método auxiliar para buscar e validar a posse do perfil de dieta
    private DietProfile findProfileForUser(Long userId, Long dietProfileId) {
        // Este método `findByIdAndUser_Id` já deve existir no seu DietProfileRepository
        return dietProfileRepository.findByIdAndUser_Id(dietProfileId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de dieta com id " + dietProfileId + " não encontrado ou não pertence ao usuário."));
    }

    // Seu método de conversão
    private MealConfigurationResponse convertToResponse(MealConfiguration meal) {
        MealConfigurationResponse response = new MealConfigurationResponse();
        response.setId(meal.getId());
        response.setMealOrder(meal.getMealOrder());
        response.setMealName(meal.getMealName());
        response.setDietProfileId(meal.getDietProfile().getId());
        return response;
    }
}