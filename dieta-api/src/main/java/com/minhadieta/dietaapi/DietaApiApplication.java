package com.minhadieta.dietaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DietaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DietaApiApplication.class, args);
	}

	@Bean // Marca este método como um bean gerenciado pelo Spring
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Implementação comum de hashing de senha
	}

}
