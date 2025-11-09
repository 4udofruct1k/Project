package com.example.project.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KinopoiskResponse {

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("items")
    private List<FilmDto> items;
}
