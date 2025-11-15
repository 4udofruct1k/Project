package com.example.project.service;

import com.example.project.model.dto.KinopoiskResponse;

public interface KinopoiskApiService {

    KinopoiskResponse searchFilms(String keyword, int page);
    FilmDetailsDto getFilmDetails(Long filmId);

}
