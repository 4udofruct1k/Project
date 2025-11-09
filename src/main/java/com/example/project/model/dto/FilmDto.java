package com.example.project.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // игнорируем ненужные поля
public class FilmDto {

    @JsonProperty("kinopoiskId")
    private Long filmId;

    @JsonProperty("nameRu")
    private String filmName;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("ratingKinopoisk")
    private Double rating;

    @JsonProperty("description")
    private String description;
}
