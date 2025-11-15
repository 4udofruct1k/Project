package com.example.project.service;

import com.example.project.model.dto.FilmDto;
import com.example.project.model.dto.KinopoiskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KinopoiskApiServiceImplTest {

    private WebClient webClient;

    private WebClient.RequestHeadersUriSpec uriSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    private KinopoiskApiServiceImpl service;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);

        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        service = new KinopoiskApiServiceImpl(webClient);
    }


    @Test
    void test_GetFilmDetails_Success() {
        FilmDto film = new FilmDto();
        film.setFilmName("Test Film");

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/{id}", 123L)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FilmDto.class)).thenReturn(Mono.just(film));

        FilmDto result = service.getFilmDetails(123L);

        assertNotNull(result);
        assertEquals("Test Film", result.getFilmName());
    }


    @Test
    void test_GetFilmDetails_ThrowsException() {

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(FilmDto.class))
                .thenThrow(WebClientResponseException.create(
                        404, "Not Found", null, null, null));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getFilmDetails(999L));

        assertTrue(ex.getMessage().contains("Ошибка при получении деталей фильма"));
    }
}
