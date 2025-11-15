package com.example.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.films")
@Data
public class FilmGenreConfig {
    private Map<String, String> genrePerDay;
}
