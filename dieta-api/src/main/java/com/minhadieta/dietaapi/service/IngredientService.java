package com.minhadieta.dietaapi.service;

import com.minhadieta.dietaapi.dto.IngredientRequest;
import com.minhadieta.dietaapi.dto.IngredientResponse;
import com.minhadieta.dietaapi.model.Ingredient;
import com.minhadieta.dietaapi.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional
    public IngredientResponse createIngredient(IngredientRequest request) {
        // Verifica se já existe um ingrediente com o mesmo nome (ignorando maiúsculas/minúsculas)
        if (ingredientRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Já existe um ingrediente com o nome " + request.getName());
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.getName());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return new IngredientResponse(savedIngredient.getId(), savedIngredient.getName());
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(ingredient -> new IngredientResponse(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IngredientResponse getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente não encontrado com o ID: " + id));
        return new IngredientResponse(ingredient.getId(), ingredient.getName());
    }

    @Transactional
    public IngredientResponse updateIngredient(Long id, IngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente não encontrado com o ID: " + id));

        // Verifica se o novo nome já está em uso por OUTRO ingrediente
        Optional<Ingredient> existingIngredient = ingredientRepository.findByNameIgnoreCase(request.getName());
        if (existingIngredient.isPresent() && !existingIngredient.get().getId().equals(id)) {
            throw new IllegalArgumentException("O nome '" + request.getName() + "' já está em uso por outro ingrediente.");
        }

        ingredient.setName(request.getName());
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return new IngredientResponse(updatedIngredient.getId(), updatedIngredient.getName());
    }

    @Transactional
    public void deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new IllegalArgumentException("Ingrediente não encontrado com o ID: " + id);
        }
        // Adicionar aqui a lógica para verificar se o ingrediente está em uso antes de deletar (futuro)
        ingredientRepository.deleteById(id);
    }
}
