package com.example.labwork.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.labwork.controllers",
		"com.example.labwork.services",
		"com.example.labwork.handlers"})
public class LabworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabworkApplication.class, args);
	}
}