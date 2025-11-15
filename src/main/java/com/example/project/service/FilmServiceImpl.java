package com.example.project.service;

import com.example.project.repository.FilmDao;
import com.example.project.mapper.FilmMapper;
import com.example.project.model.dto.FilmDto;
import com.example.project.model.entity.Film;
import com.example.project.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final FilmDao filmDao;
    private final KinopoiskApiService kinopoiskApiService;
    private final FilmMapper filmMapper;

    @Override
    public void saveNewFilms(List<FilmDto> films) {
        for (FilmDto dto : films) {
            Optional<Film> existing = filmRepository.findByFilmId(dto.getFilmId());
            if (existing.isEmpty()) {
                FilmDto details = kinopoiskApiService.getFilmDetails(dto.getFilmId());
                filmRepository.save(filmMapper.DtoToFilm(details));
            }
        }
    }

    @Override
    public Page<Film> searchFilms(String filmName,
                                  Integer year,
                                  Double minRating,
                                  Double maxRating,
                                  int page,
                                  int size,
                                  String sortBy,
                                  String direction) {

        return filmDao.findByFilters(filmName, year, minRating, maxRating, page, size, sortBy, direction);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public void clearAllFilms() {
        filmRepository.deleteAll();
    }
}
