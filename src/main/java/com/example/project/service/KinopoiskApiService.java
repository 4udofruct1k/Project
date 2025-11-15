package com.example.project.service;

import com.example.project.model.dto.FilmDto;
import com.example.project.model.dto.KinopoiskResponse;

public interface KinopoiskApiService {

    KinopoiskResponse searchFilms(String keyword, int page);
    FilmDto getFilmDetails(Long filmId);
    KinopoiskResponse searchFilmsByGenre(String genre, int page);

}
