package com.example.project.repository;

import com.example.project.model.entity.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final FilmRepository filmRepository;

    @Override
    public Page<Film> findByFilters(String filmName,
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

        Specification<Film> spec = (root, query, cb) -> cb.conjunction();

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
}
