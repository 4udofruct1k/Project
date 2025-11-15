package com.example.project.service;

import com.example.project.mapper.FilmMapper;
import com.example.project.model.dto.FilmDto;
import com.example.project.model.entity.Film;
import com.example.project.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    @KafkaListener(topics = "${app.kafka.topic.films}", groupId = "film-group")
    public void consume(FilmDto filmDto) {
        System.out.println("Received: " + filmDto.getFilmName());

        Optional<Film> existing = filmRepository.findByFilmId(filmDto.getFilmId());
        if (existing.isEmpty()) {
            Film film = filmMapper.DtoToFilm(filmDto);
            filmRepository.save(film);
            System.out.println("Saved: " + film.getFilmName());
        } else {
            System.out.println("Film already exists: " + filmDto.getFilmName());
        }
    }
}
