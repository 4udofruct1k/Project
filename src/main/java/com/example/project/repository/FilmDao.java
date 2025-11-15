package com.example.project.repository;

import com.example.project.model.entity.Film;
import org.springframework.data.domain.Page;

public interface FilmDao {
    Page<Film> findByFilters(
            String filmName,
            Integer year,
            Double minRating,
            Double maxRating,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
