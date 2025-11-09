package com.example.project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "films")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // внутренний ID в базе данных

    @Column(name = "film_id", unique = true, nullable = false)
    private Long filmId; // ID фильма из Кинопоиска

    @Column(name = "film_name")
    private String filmName;

    @Column(name = "year")
    private Integer year;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
