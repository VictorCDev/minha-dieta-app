package com.minhadieta.dietaapi.controller;

import com.minhadieta.dietaapi.dto.UserRequest;
import com.minhadieta.dietaapi.model.AppUser;
import com.minhadieta.dietaapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            AppUser newUser = userService.createUser(userRequest);
            // Retorna o usuário criado com status HTTP 201 (Created)
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Se houver um erro de validação (ex: nome de usuário ou email já existe)
            // Retorna uma mensagem de erro com status HTTP 400 (Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Lida com outras exceções inesperadas
            return new ResponseEntity<>("Ocorreu um erro inesperado ao registrar o usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.findAll(); // Chama o método findAll no seu UserService
        return ResponseEntity.ok(users); // Retorna a lista de usuários com status 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        try {
            AppUser updatedUser = userService.updateUser(id, userRequest);
            return ResponseEntity.ok(updatedUser); // Retorna 200 OK com o usuário atualizado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna 400 Bad Request em caso de erro
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro inesperado ao atualizar o usuário.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            // Retorna 204 No Content para indicar que a operação foi bem-sucedida e não há conteúdo para retornar
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o usuário não for encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro inesperado ao deletar o usuário.");
        }
    }
}
