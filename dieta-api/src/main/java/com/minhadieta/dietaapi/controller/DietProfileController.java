package com.minhadieta.dietaapi.controller;

import com.minhadieta.dietaapi.dto.DietProfileRequest;
import com.minhadieta.dietaapi.dto.DietProfileResponse;
import com.minhadieta.dietaapi.service.DietProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/diet-profiles")
public class DietProfileController {

    private DietProfileService dietProfileService;

    @Autowired
    public DietProfileController(DietProfileService dietProfileService) {
        this.dietProfileService = dietProfileService;
    }

    // Endpoint para criar um novo perfil de dieta
    @PostMapping
    public ResponseEntity<DietProfileResponse> createDietProfile(
            @PathVariable Long userId,
            @Valid @RequestBody DietProfileRequest request) {
        try {
            DietProfileResponse response = dietProfileService.createDietProfile(userId, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Captura exceções de validação ou de "usuário não encontrado"
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        }
    }

    // Endpoint para listar todos os perfis de dieta de um usuário
    @GetMapping
    public ResponseEntity<List<DietProfileResponse>> getDietProfilesByUser(@PathVariable Long userId) {
        try {
            List<DietProfileResponse> profiles = dietProfileService.getDietProfilesByUser(userId);
            return ResponseEntity.ok(profiles); // Retorna 200 OK com a lista de perfis
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Se o usuário não for encontrado, retorna 404 Not Found
        }
    }

    // Endpoint para buscar um perfil de dieta por ID (garantindo que pertence ao usuário)
    @GetMapping("/{profileId}")
    public ResponseEntity<DietProfileResponse> getDietProfileByIdAndUser(
            @PathVariable Long profileId,
            @PathVariable Long userId) {
        try {
            DietProfileResponse profile = dietProfileService.getDietProfileByIdAndUser(profileId, userId);
            return ResponseEntity.ok(profile); // Retorna 200 OK com o perfil
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Se o perfil não for encontrado ou não pertencer ao usuário, retorna 404 Not Found
        }
    }

    // Endpoint para atualizar um perfil de dieta
    @PutMapping("/{profileId}")
    public ResponseEntity<DietProfileResponse> updateDietProfile(
            @PathVariable Long profileId,
            @PathVariable Long userId,
            @Valid @RequestBody DietProfileRequest request) {
        try {
            DietProfileResponse response = dietProfileService.updateDietProfile(profileId, userId, request);
            return ResponseEntity.ok(response); // Retorna 200 OK com o perfil atualizado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request (Nome duplicado, etc.)
        }
    }

    // Endpoint para deletar um perfil de dieta
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteDietProfile(
            @PathVariable Long profileId,
            @PathVariable Long userId) {
        try {
            dietProfileService.deleteDietProfile(profileId, userId);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content para deleção bem-sucedida
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request (Não pode deletar último ativo, etc.)
        }
    }

    // Endpoint para ativar um perfil de dieta
    @PutMapping("/{profileId}/activate")
    public ResponseEntity<DietProfileResponse> activateDietProfile(
            @PathVariable Long profileId,
            @PathVariable Long userId) {
        try {
            DietProfileResponse response = dietProfileService.activateDietProfile(profileId, userId);
            return ResponseEntity.ok(response); // Retorna 200 OK com o perfil ativado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request
        }
    }
}
