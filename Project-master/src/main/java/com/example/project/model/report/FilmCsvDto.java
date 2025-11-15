package com.example.project.model.report;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmCsvDto {
    private Long filmId;
    private String filmName;
    private Integer year;
    private Double rating;
}
