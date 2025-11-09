package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.project.repository")
@EntityScan(basePackages = "com.example.project.model.entity")
@ComponentScan(basePackages = "com.example")
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}