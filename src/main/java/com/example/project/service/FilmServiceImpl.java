package com.example.project.service;

import com.example.project.model.dto.FilmDetailsDto;
import com.example.project.model.dto.FilmDto;
import com.example.project.model.entity.Film;
import com.example.project.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final KinopoiskApiService kinopoiskApiService;



    @Override
    public void saveNewFilms(List<FilmDto> films) {
        for (FilmDto dto : films) {
            Optional<Film> existing = filmRepository.findByFilmId(dto.getFilmId());
            if (existing.isEmpty()) {

                FilmDetailsDto details = kinopoiskApiService.getFilmDetails(dto.getFilmId());

                Film film = new Film();
                film.setFilmId(dto.getFilmId());
                film.setFilmName(dto.getFilmName());
                film.setYear(dto.getYear());
                film.setRating(dto.getRating());
                film.setDescription(details.getDescription());

                filmRepository.save(film);
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

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Film> spec = (root, query, cb) -> cb.conjunction(); // стартовая точка

        if (filmName != null && !filmName.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("filmName")), "%" + filmName.toLowerCase() + "%"));
        }

        if (year != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("year"), year));
        }

        if (minRating != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("rating"), minRating));
        }

        if (maxRating != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("rating"), maxRating));
        }

        return filmRepository.findAll(spec, pageable);
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
