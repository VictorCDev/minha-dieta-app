package com.minhadieta.dietaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diet_profile")
public class DietProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_name", nullable = false, length = 100)
    private String profileName; // Ex: "Cutting", "Bulking", "Manutenção"

    @Column(name = "goal", length = 255)
    private String goal; // Uma descrição mais detalhada do objetivo do perfil

    // Relacionamento Muitos-para-Um com Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user; // O usuário a quem este perfil de dieta pertence

    @Column(name = "is_active", nullable = false)
        private Boolean isActive = false; // Indica se este perfil está ativo para o usuário no momento
}
