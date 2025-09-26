package com.minhadieta.dietaapi.controller;

import com.minhadieta.dietaapi.dto.MealItemRequest;
import com.minhadieta.dietaapi.dto.MealItemResponse;
import com.minhadieta.dietaapi.service.MealItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/diet-profiles/{dietProfileId}/meals/{mealId}/items")
public class MealItemController {

    @Autowired
    private MealItemService mealItemService;

    @PostMapping
    public ResponseEntity<?> createMealItem(
            @PathVariable Long userId,
            @PathVariable Long dietProfileId,
            @PathVariable Long mealId,
            @Valid @RequestBody MealItemRequest request) {
        try {
            MealItemResponse response = mealItemService.create(userId, dietProfileId, mealId, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllItemsForMeal(
            @PathVariable Long userId,
            @PathVariable Long dietProfileId,
            @PathVariable Long mealId) {
        try {
            List<MealItemResponse> items = mealItemService.findAllByMeal(userId, dietProfileId, mealId);
            return ResponseEntity.ok(items);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateMealItem(
            @PathVariable Long userId,
            @PathVariable Long dietProfileId,
            @PathVariable Long mealId,
            @PathVariable Long itemId,
            @Valid @RequestBody MealItemRequest request) {
        try {
            MealItemResponse response = mealItemService.update(userId, dietProfileId, mealId, itemId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteMealItem(
            @PathVariable Long userId,
            @PathVariable Long dietProfileId,
            @PathVariable Long mealId,
            @PathVariable Long itemId) {
        try {
            mealItemService.delete(userId, dietProfileId, mealId, itemId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
