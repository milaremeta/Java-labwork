package com.example.labwork.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.labwork.controllers", "com.example.labwork.services", "com.example.labwork.handlers", "com.example.labwork.config", "com.example.labwork.security"})
public class LabworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabworkApplication.class, args);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**").permitAll()
						.anyRequest().permitAll()
				);
		return http.build();
	}
}