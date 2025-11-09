package com.example.project.service;

import com.example.project.model.dto.FilmDetailsDto;
import com.example.project.model.dto.KinopoiskResponse;
import com.example.project.service.KinopoiskApiService;
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
    public FilmDetailsDto getFilmDetails(Long filmId) {
        try {
            return webClientWithTimeout.get()
                    .uri("/{id}", filmId)
                    .retrieve()
                    .bodyToMono(FilmDetailsDto.class)
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
