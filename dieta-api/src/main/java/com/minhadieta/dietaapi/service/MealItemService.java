package com.minhadieta.dietaapi.service;

import com.minhadieta.dietaapi.dto.MealItemRequest;
import com.minhadieta.dietaapi.dto.MealItemResponse;
import com.minhadieta.dietaapi.model.Ingredient;
import com.minhadieta.dietaapi.model.MealConfiguration;
import com.minhadieta.dietaapi.model.MealItem;
import com.minhadieta.dietaapi.repository.IngredientRepository;
import com.minhadieta.dietaapi.repository.MealConfigurationRepository;
import com.minhadieta.dietaapi.repository.MealItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealItemService {

    @Autowired
    private MealItemRepository mealItemRepository;

    @Autowired
    private MealConfigurationRepository mealConfigurationRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional
    public MealItemResponse create(Long userId, Long dietProfileId, Long mealId, MealItemRequest request) {
        // 1. Valida a posse e busca a refeição
        MealConfiguration mealConfig = findMealConfigForUser(userId, dietProfileId, mealId);

        // 2. Busca o ingrediente
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente com ID " + request.getIngredientId() + " não encontrado."));

        // 3. Cria e salva o novo item da refeição
        MealItem newMealItem = new MealItem();
        newMealItem.setMealConfiguration(mealConfig);
        newMealItem.setIngredient(ingredient);
        newMealItem.setQuantity(request.getQuantity());
        newMealItem.setUnit(request.getUnit());

        MealItem savedItem = mealItemRepository.save(newMealItem);
        return convertToResponse(savedItem);
    }


    @Transactional(readOnly = true)
    public List<MealItemResponse> findAllByMeal(Long userId, Long dietProfileId, Long mealId) {
        MealConfiguration mealConfig = findMealConfigForUser(userId, dietProfileId, mealId);
        return mealConfig.getMealItems().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MealItemResponse update(Long userId, Long dietProfileId, Long mealId, Long itemId, MealItemRequest request) {
        findMealConfigForUser(userId, dietProfileId, mealId); // Apenas para validação de segurança

        MealItem mealItem = mealItemRepository.findByIdAndMealConfiguration_Id(itemId, mealId)
                .orElseThrow(() -> new IllegalArgumentException("Item de refeição não encontrado ou não pertence a esta refeição."));

        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente com ID " + request.getIngredientId() + " não encontrado."));

        mealItem.setIngredient(ingredient);
        mealItem.setQuantity(request.getQuantity());
        mealItem.setUnit(request.getUnit());

        MealItem updatedItem = mealItemRepository.save(mealItem);
        return convertToResponse(updatedItem);
    }

    @Transactional
    public void delete(Long userId, Long dietProfileId, Long mealId, Long itemId) {
        findMealConfigForUser(userId, dietProfileId, mealId); // Validação de segurança

        MealItem mealItem = mealItemRepository.findByIdAndMealConfiguration_Id(itemId, mealId)
                .orElseThrow(() -> new IllegalArgumentException("Item de refeição não encontrado ou não pertence a esta refeição."));

        mealItemRepository.delete(mealItem);
    }


    /**
     * Método auxiliar de segurança: Busca uma MealConfiguration pelo seu ID, mas garante
     * que ela pertence ao DietProfile e ao User especificados.
     */
    private MealConfiguration findMealConfigForUser(Long userId, Long dietProfileId, Long mealId) {
        return mealConfigurationRepository.findByIdAndDietProfile_IdAndDietProfile_User_Id(mealId, dietProfileId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Refeição não encontrada ou não pertence ao perfil/usuário especificado."));
    }

    /**
     * Método auxiliar para converter a entidade MealItem para o DTO de resposta.
     */
    private MealItemResponse convertToResponse(MealItem mealItem) {
        return new MealItemResponse(
                mealItem.getId(),
                mealItem.getIngredient().getId(),
                mealItem.getIngredient().getName(),
                mealItem.getQuantity(),
                mealItem.getUnit()
        );
    }
}
