package com.minhadieta.dietaapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Importação adicionada
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importação adicionada
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy; // Importação adicionada

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF para APIs REST sem sessões baseadas em cookies
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Garante que as sessões são stateless (sempre importante com JWT)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/register").permitAll() // Permite acesso público ao endpoint de registro
                        .requestMatchers("/api/auth/login").permitAll() // Permite acesso público ao endpoint de login (NOVO)
                        .requestMatchers("/api/users").permitAll() // Permite acesso público ao endpoint de pesquisar usuarios - *CUIDADO: APENAS PARA TESTE INICIAL. REMOVER DEPOIS!*
                        .requestMatchers("/api/users/{userId}").permitAll() // Permite acesso público ao endpoint de pesquisar, atualizar, deletar por ID
                        // Permite acesso público para o método POST na URL de perfis de dieta para QUALQUER usuário (Mantenha para testes iniciais, mas proteger depois)
                        .requestMatchers(HttpMethod.POST, "/api/users/{usuarioId}/perfis-dieta").permitAll()
                        // Permite acesso público para o método GET na URL de perfis de dieta para QUALQUER usuário (Mantenha para testes iniciais, mas proteger depois)
                        .requestMatchers(HttpMethod.GET, "/api/users/{usuarioId}/perfis-dieta").permitAll()
                        .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
                )
                .httpBasic(withDefaults()); // Habilita autenticação HTTP Basic (opcional, mas bom para testes iniciais)
        // .formLogin(withDefaults()); // Opcional: Se for usar formulário de login (não para APIs REST sem JWT)
        return http.build();
    }

    // Bean para expor o AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
