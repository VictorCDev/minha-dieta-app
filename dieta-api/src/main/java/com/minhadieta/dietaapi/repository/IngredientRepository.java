package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Exemplo de método personalizado:
    // Encontra um ingrediente pelo nome (útil para verificar se já existe antes de adicionar um novo)
    Optional<Ingredient> findByName(String name);
}
