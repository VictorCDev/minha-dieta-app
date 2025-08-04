package com.minhadieta.dietaapi.repository;

import com.minhadieta.dietaapi.model.WeeklyDiet;
import com.minhadieta.dietaapi.model.DietProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyDietRepository extends JpaRepository<WeeklyDiet, Long> {

    // Método ESSENCIAL: Encontra uma dieta semanal pelo seu ID
    // E garante que ela pertence ao PerfilDieta de um Usuário específico (pelo ID do usuário).
    // Este é um método robusto para verificar posse e buscar um item específico.
    Optional<WeeklyDiet> findByIdAndDietProfile_User_Id(Long id, Long userId);

    // Método BÁSICO: Encontra todas as dietas semanais associadas a um PerfilDieta específico.
    // Muito útil para carregar todas as dietas de um perfil.
    List<WeeklyDiet> findByDietProfile(DietProfile dietProfile);

}
