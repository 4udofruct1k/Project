package com.example.project.service;

import com.example.project.model.dto.FilmDto;
import com.example.project.model.entity.Film;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FilmService {

    void saveNewFilms(List<FilmDto> films);

    Page<Film> searchFilms(String filmName,
                           Integer year,
                           Double minRating,
                           Double maxRating,
                           int page,
                           int size,
                           String sortBy,
                           String direction);

    List<Film> getAllFilms();

    void clearAllFilms();

}