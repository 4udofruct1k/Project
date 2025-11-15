package com.example.project.integration;

import com.example.project.model.dto.FilmDto;
import com.example.project.model.dto.KinopoiskResponse;
import com.example.project.model.entity.Film;
import com.example.project.repository.FilmRepository;
import com.example.project.service.KinopoiskApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false"
})
class FilmLoadIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private KinopoiskApiService kinopoiskApiService; // теперь через TestConfiguration

    private FilmDto testFilmDto;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public KinopoiskApiService kinopoiskApiService() {
            return Mockito.mock(KinopoiskApiService.class);
        }
    }

    @BeforeEach
    void setUp() {
        filmRepository.deleteAll();

        testFilmDto = new FilmDto();
        testFilmDto.setFilmId(1L);
        testFilmDto.setFilmName("Test Film");
        testFilmDto.setYear(2023);
        testFilmDto.setRating(8.5);
        testFilmDto.setDescription("Test description");
    }

    @Test
    void loadFilmsFromKinopoisk_shouldSaveFilmsAndReturnJson() throws Exception {
        KinopoiskResponse response = new KinopoiskResponse();
        response.setItems(List.of(testFilmDto));

        Mockito.when(kinopoiskApiService.searchFilms("Test", 1)).thenReturn(response);
        Mockito.when(kinopoiskApiService.getFilmDetails(1L)).thenReturn(testFilmDto);

        mockMvc.perform(get("/api/v2/films/load")
                        .param("keyword", "Test")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].nameRu").value("Test Film"))
                .andExpect(jsonPath("$.items[0].year").value(2023))
                .andExpect(jsonPath("$.items[0].ratingKinopoisk").value(8.5))
                .andExpect(jsonPath("$.items[0].description").value("Test description"))
                .andExpect(jsonPath("$.items[0].kinopoiskId").value(1));



        List<Film> filmsInDb = filmRepository.findAll();
        assert(filmsInDb.size() == 1);
        assert(filmsInDb.get(0).getFilmId().equals(1L));
        assert(filmsInDb.get(0).getFilmName().equals("Test Film"));
    }

    @Test
    void loadFilmsFromKinopoisk_shouldHandleEmptyResponse() throws Exception {
        KinopoiskResponse response = new KinopoiskResponse();
        response.setItems(List.of());

        Mockito.when(kinopoiskApiService.searchFilms("Empty", 1)).thenReturn(response);

        mockMvc.perform(get("/api/v2/films/load")
                        .param("keyword", "Empty")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)));

        assert(filmRepository.findAll().isEmpty());
    }

    @Test
    void loadFilmsFromKinopoisk_shouldNotSaveDuplicates() throws Exception {
        Film existing = new Film(null, 1L, "Test Film", 2023, 8.5, "Test description");
        filmRepository.save(existing);

        KinopoiskResponse response = new KinopoiskResponse();
        response.setItems(List.of(testFilmDto));

        Mockito.when(kinopoiskApiService.searchFilms("Test", 1)).thenReturn(response);
        Mockito.when(kinopoiskApiService.getFilmDetails(1L)).thenReturn(testFilmDto);

        mockMvc.perform(get("/api/v2/films/load")
                        .param("keyword", "Test")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)));

        assert(filmRepository.findAll().size() == 1);
    }

}
