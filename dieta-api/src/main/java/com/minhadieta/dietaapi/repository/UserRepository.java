package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    // JpaRepository fornece automaticamente métodos CRUD (Create, Read, Update, Delete)
    // como save(), findById(), findAll(), delete() para a entidade User.

    // Você pode adicionar métodos personalizados aqui, se precisar.
    // Exemplo:
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
}
