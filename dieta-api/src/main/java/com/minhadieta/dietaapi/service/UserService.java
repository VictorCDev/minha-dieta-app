package com.minhadieta.dietaapi.service;

import com.minhadieta.dietaapi.model.AppUser;
import com.minhadieta.dietaapi.repository.UserRepository;
import com.minhadieta.dietaapi.dto.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.minhadieta.dietaapi.dto.LoginRequest; // Importação adicionada
import com.minhadieta.dietaapi.dto.AuthResponse; // Importação adicionada
import com.minhadieta.dietaapi.security.JwtUtil; // Importação adicionada
import org.springframework.security.core.userdetails.UserDetailsService; // Importação adicionada
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importação adicionada
import org.springframework.security.authentication.AuthenticationManager; // Importação adicionada
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importação adicionada
import org.springframework.security.core.Authentication; // Importação adicionada
import org.springframework.security.core.context.SecurityContextHolder; // Importação adicionada


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // Implementação do método loadUserByUsername da interface UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o nome: " + username));

        // Retorna um objeto User do Spring Security
        return new User(user.getUsername(), user.getPasswordHash(), new ArrayList<>()); // No momento, sem roles/authorities
    }

    @Transactional
    public AppUser createUser(UserRequest userRequest) {
        // Verifica se já existe um usuário com o mesmo nome de usuário ou email
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já existe.");
        }
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        // Cria uma nova instância da entidade User
        AppUser newUser = new AppUser();
        newUser.setUsername(userRequest.getUsername());
        // Hasheia a senha antes de salvar no banco de dados
        newUser.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setEmail(userRequest.getEmail());
        newUser.setRegistrationDate(LocalDateTime.now()); // Define a data de cadastro atual

        // Salva o novo usuário no banco de dados
        return userRepository.save(newUser);
    }

    @Transactional
    public AppUser updateUser(Long id, UserRequest userRequest) {
        // 1. Encontra o usuário existente pelo ID
        AppUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));

        // 2. Verifica se o novo nome de usuário ou email já existe (se houver alteração)
        if (!existingUser.getUsername().equals(userRequest.getUsername())) {
            if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Nome de usuário '" + userRequest.getUsername() + "' já está em uso.");
            }
            existingUser.setUsername(userRequest.getUsername());
        }

        if (!existingUser.getEmail().equals(userRequest.getEmail())) {
            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email '" + userRequest.getEmail() + "' já está cadastrado.");
            }
            existingUser.setEmail(userRequest.getEmail());
        }

        // 3. Atualiza a senha apenas se uma nova senha for fornecida na requisição
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        }

        // 4. Salva as alterações no banco de dados
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        // Verifica se o usuário existe antes de tentar deletar
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Método para encontrar um usuário pelo ID
    public Optional<AppUser> findUserById(Long id) {
        return userRepository.findById(id);
    }


    public List<AppUser> findAll() {
        return userRepository.findAll(); // Simplesmente delega para o JpaRepository
    }

    // NOVO MÉTODO: Lógica de Login
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Tenta autenticar o usuário com o AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Se a autenticação for bem-sucedida, define-a no SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Carrega os detalhes completos do usuário para gerar o JWT e a resposta
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AppUser loggedInUser = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação."));

            // Gera o token JWT
            String jwt = jwtUtil.generateToken(userDetails);

            // Retorna o token e os detalhes do usuário
            return new AuthResponse(jwt, loggedInUser.getId(), loggedInUser.getUsername(), loggedInUser.getEmail());

        } catch (Exception e) {
            // Captura exceções de autenticação (ex: BadCredentialsException)
            throw new IllegalArgumentException("Credenciais inválidas: " + e.getMessage());
        }
    }

    // Você pode adicionar outros métodos aqui, como findUserByUsername, etc.
//    public Optional<User> findUserByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
}
