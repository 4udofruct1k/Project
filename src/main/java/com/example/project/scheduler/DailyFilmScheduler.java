package com.example.project.scheduler;

import com.example.project.model.dto.FilmDto;
import com.example.project.service.KafkaProducerService;
import com.example.project.service.KinopoiskApiService;
import com.example.project.config.FilmGenreConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyFilmScheduler {

    private final KinopoiskApiService kinopoiskApiService;
    private final KafkaProducerService kafkaProducerService;
    private final FilmGenreConfig filmGenreConfig;

    @Scheduled(cron = "0 */1 * * * *")
    public void sendDailyFilms() {
        String day = LocalDate.now().getDayOfWeek().name().toLowerCase();
        String genre = filmGenreConfig.getGenrePerDay().get(day);

        if (genre == null) {
            log.warn("Нет жанра для дня: {}", day);
            return;
        }

        log.info("Ищем фильмы жанра '{}' для {}", genre, day);

        List<FilmDto> films = kinopoiskApiService.searchFilmsByGenre(genre, 1).getItems();
        if (films.size() > 50) {
            films = films.subList(0, 50);
        }

        films.forEach(film -> {
            kafkaProducerService.sendFilm(film);
            log.info("Отправлен фильм: {}", film.getFilmName());
        });
    }
}
