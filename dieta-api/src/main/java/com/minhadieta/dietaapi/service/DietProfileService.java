package com.minhadieta.dietaapi.service;

import com.minhadieta.dietaapi.dto.DietProfileRequest;
import com.minhadieta.dietaapi.dto.DietProfileResponse;
import com.minhadieta.dietaapi.model.DietProfile;
import com.minhadieta.dietaapi.model.AppUser;
import com.minhadieta.dietaapi.repository.DietProfileRepository;
import com.minhadieta.dietaapi.repository.UserRepository; // Para buscar o usuário
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DietProfileService {

    private final DietProfileRepository dietProfileRepository;
    private final UserRepository userRepository; // Para buscar o usuário associado ao perfil

    @Autowired
    public DietProfileService(DietProfileRepository dietProfileRepository, UserRepository userRepository) {
        this.dietProfileRepository = dietProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DietProfileResponse createDietProfile(Long userId, DietProfileRequest request) {
        // 1. Verificar se o usuário existe
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + userId));

        // 2. Verificar se já existe um perfil com o mesmo nome para este usuário
        if (dietProfileRepository.findByProfileNameAndUser(request.getProfileName(), user).isPresent()) {
            throw new IllegalArgumentException("Já existe um perfil com este nome para o usuário.");
        }

        // 3. Criar a nova entidade DietProfile
        DietProfile dietProfile = new DietProfile();
        dietProfile.setProfileName(request.getProfileName());
        dietProfile.setGoal(request.getGoal());
        dietProfile.setUser(user);
        dietProfile.setIsActive(false); // Por padrão, o novo perfil não é ativo

        // 4. Se este for o primeiro perfil do usuário, torná-lo ativo automaticamente
        if (dietProfileRepository.findByUser(user).isEmpty()) {
            dietProfile.setIsActive(true);
        }

        DietProfile savedProfile = dietProfileRepository.save(dietProfile);
        return convertToResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public List<DietProfileResponse> getDietProfilesByUser(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + userId));

        List<DietProfile> profiles = dietProfileRepository.findByUser(user);
        return profiles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DietProfileResponse getDietProfileByIdAndUser(Long profileId, Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + userId));

        DietProfile profile = dietProfileRepository.findByIdAndUser(profileId, user)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de dieta não encontrado ou não pertence a este usuário."));

        return convertToResponse(profile);
    }

    @Transactional
    public DietProfileResponse updateDietProfile(Long profileId, Long userId, DietProfileRequest request) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + userId));

        DietProfile profile = dietProfileRepository.findByIdAndUser(profileId, user)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de dieta não encontrado ou não pertence a este usuário."));

        // Verificar se o novo nome de perfil já existe para este usuário (excluindo o próprio perfil que está sendo atualizado)
        Optional<DietProfile> existingProfileWithName = dietProfileRepository.findByProfileNameAndUser(request.getProfileName(), user);
        if (existingProfileWithName.isPresent() && !existingProfileWithName.get().getId().equals(profileId)) {
            throw new IllegalArgumentException("Já existe outro perfil com este nome para o usuário.");
        }

        profile.setProfileName(request.getProfileName());
        profile.setGoal(request.getGoal());

        DietProfile updatedProfile = dietProfileRepository.save(profile);
        return convertToResponse(updatedProfile);
    }

    @Transactional
    public void deleteDietProfile(Long profileId, Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + userId));

        DietProfile profile = dietProfileRepository.findByIdAndUser(profileId, user)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de dieta não encontrado ou não pertence a este usuário."));

        // Lógica de segurança: Não permitir deletar o último perfil ativo
        if (profile.getIsActive() && dietProfileRepository.findByUser(user).size() == 1) {
            throw new IllegalArgumentException("Não é possível deletar o único perfil ativo do usuário.");
        }

        // Se o perfil a ser deletado for o ativo, e houver outros perfis, ativar o mais antigo não ativo
        if (profile.getIsActive()) {
            List<DietProfile> otherProfiles = dietProfileRepository.findByUser(user).stream()
                    .filter(p -> !p.getId().equals(profileId))
                    .collect(Collectors.toList());

            if (!otherProfiles.isEmpty()) {
                // Ativar o primeiro perfil não ativo encontrado
                otherProfiles.get(0).setIsActive(true);
                dietProfileRepository.save(otherProfiles.get(0));
            }
        }
        dietProfileRepository.delete(profile);
    }

    @Transactional
    public DietProfileResponse activateDietProfile(Long profileId, Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + userId));

        DietProfile profileToActivate = dietProfileRepository.findByIdAndUser(profileId, user)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de dieta não encontrado ou não pertence a este usuário."));

        if (profileToActivate.getIsActive()) {
            return convertToResponse(profileToActivate); // Já está ativo, não faz nada
        }

        // Desativar qualquer outro perfil ativo para este usuário
        Optional<DietProfile> currentActiveProfile = dietProfileRepository.findByUserAndIsActiveTrue(user);
        currentActiveProfile.ifPresent(p -> {
            p.setIsActive(false);
            dietProfileRepository.save(p);
        });

        // Ativar o perfil selecionado
        profileToActivate.setIsActive(true);
        DietProfile updatedProfile = dietProfileRepository.save(profileToActivate);
        return convertToResponse(updatedProfile);
    }

    // Método auxiliar para converter entidade em DTO de resposta
    private DietProfileResponse convertToResponse(DietProfile dietProfile) {
        DietProfileResponse response = new DietProfileResponse();
        response.setId(dietProfile.getId());
        response.setProfileName(dietProfile.getProfileName());
        response.setGoal(dietProfile.getGoal());
        response.setUserId(dietProfile.getUser().getId()); // Retorna o ID do usuário
        response.setIsActive(dietProfile.getIsActive());
        return response;
    }

}
