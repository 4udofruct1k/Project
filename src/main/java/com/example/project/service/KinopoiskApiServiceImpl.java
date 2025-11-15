package com.example.project.service;

import com.example.project.model.dto.FilmDto;
import com.example.project.model.dto.KinopoiskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KinopoiskApiServiceImpl implements KinopoiskApiService {

    private final WebClient webClientWithTimeout;

    @Override
    public FilmDto getFilmDetails(Long filmId) {
        try {
            return webClientWithTimeout.get()
                    .uri("/{id}", filmId)
                    .retrieve()
                    .bodyToMono(FilmDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Ошибка при получении деталей фильма {}: {} {}", filmId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Ошибка при получении деталей фильма: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Неизвестная ошибка при получении деталей фильма {}", filmId, e);
            throw new RuntimeException("Ошибка при получении данных фильма", e);
        }
    }

    @Override
    public KinopoiskResponse searchFilmsByGenre(String genre, int page) {
        try {
            return webClientWithTimeout.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/movie")
                            .queryParam("genres.name", genre)
                            .queryParam("page", page)
                            .build())
                    .header("X-API-KEY", "<ваш API ключ>") // ключ из пропертей или @Value
                    .retrieve()
                    .bodyToMono(KinopoiskResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Ошибка при получении фильмов по жанру {}", genre, e);
            throw new RuntimeException("Ошибка при получении данных", e);
        }
    }


    @Override
    public KinopoiskResponse searchFilms(String keyword, int page) {
        try {
            String json = webClientWithTimeout.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("keyword", keyword)
                            .queryParam("page", page)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Kinopoisk API response: " + json);
            return webClientWithTimeout.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("keyword", keyword)
                            .queryParam("page", page)
                            .build())
                    .retrieve()
                    .bodyToMono(KinopoiskResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Ошибка при обращении к API Кинопоиска: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Ошибка при запросе к API Кинопоиска: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Неизвестная ошибка при обращении к API Кинопоиска", e);
            throw new RuntimeException("Ошибка при получении данных", e);
        }
    }
}
