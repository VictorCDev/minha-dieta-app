package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.AppUser;
import com.minhadieta.dietaapi.model.DietProfile;
import com.minhadieta.dietaapi.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietProfileRepository extends JpaRepository<DietProfile, Long> {
    // Retorna todos os perfis de dieta para um usuário específico
    List<DietProfile> findByUser(AppUser user);

    // Retorna um perfil de dieta por ID e usuário (para garantir que um usuário só acesse seus próprios perfis)
    Optional<DietProfile> findByIdAndUser(Long id, AppUser user);

    // Retorna um perfil de dieta por nome e usuário (útil para verificar duplicidade de nomes de perfil para o mesmo usuário)
    Optional<DietProfile> findByProfileNameAndUser(String profileName, AppUser user);

    // Retorna o perfil ativo para um usuário específico
    Optional<DietProfile> findByUserAndIsActiveTrue(AppUser user);

    // Método para buscar perfis de dieta por ID de usuário (equivalente ao findByUsuario_Id)
    List<DietProfile> findByUser_Id(Long userId);
}