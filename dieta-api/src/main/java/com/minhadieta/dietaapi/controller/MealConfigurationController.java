package com.minhadieta.dietaapi.controller;

import com.minhadieta.dietaapi.dto.MealConfigurationRequest;
import com.minhadieta.dietaapi.dto.MealConfigurationResponse;
import com.minhadieta.dietaapi.service.MealConfigurationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/diet-profiles/{dietProfileId}/meals")
public class MealConfigurationController {

    @Autowired
    private MealConfigurationService mealConfigurationService;

    @PostMapping
    public ResponseEntity<?> createMealConfiguration(@PathVariable Long userId, @PathVariable Long dietProfileId, @Valid @RequestBody MealConfigurationRequest request) {
        try {
            MealConfigurationResponse response = mealConfigurationService.create(userId, dietProfileId, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMealsByDietProfile(@PathVariable Long userId, @PathVariable Long dietProfileId) {
        try {
            List<MealConfigurationResponse> meals = mealConfigurationService.findByDietProfile(userId, dietProfileId);
            return ResponseEntity.ok(meals);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ENDPOINT NOVO
    @GetMapping("/{mealId}")
    public ResponseEntity<?> getMealById(@PathVariable Long userId, @PathVariable Long dietProfileId, @PathVariable Long mealId) {
        try {
            MealConfigurationResponse meal = mealConfigurationService.findById(userId, dietProfileId, mealId);
            return ResponseEntity.ok(meal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ENDPOINT NOVO
    @PutMapping("/{mealId}")
    public ResponseEntity<?> updateMeal(@PathVariable Long userId, @PathVariable Long dietProfileId, @PathVariable Long mealId, @Valid @RequestBody MealConfigurationRequest request) {
        try {
            MealConfigurationResponse response = mealConfigurationService.update(userId, dietProfileId, mealId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ENDPOINT NOVO
    @DeleteMapping("/{mealId}")
    public ResponseEntity<?> deleteMeal(@PathVariable Long userId, @PathVariable Long dietProfileId, @PathVariable Long mealId) {
        try {
            mealConfigurationService.delete(userId, dietProfileId, mealId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}