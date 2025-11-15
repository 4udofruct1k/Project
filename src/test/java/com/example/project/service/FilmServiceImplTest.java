package com.example.project.service;

import com.example.project.mapper.FilmMapper;
import com.example.project.model.dto.FilmDto;
import com.example.project.model.entity.Film;
import com.example.project.repository.FilmDao;
import com.example.project.repository.FilmRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class FilmServiceImplTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private FilmDao filmDao;

    @Mock
    private KinopoiskApiService kinopoiskApiService;

    @Mock
    private FilmMapper filmMapper;

    @InjectMocks
    private FilmServiceImpl filmService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void test_saveNewFilms_whenFilmExists_shouldNotSave(){
        FilmDto filmDto = new FilmDto();
        filmDto.setFilmId(1L);

        Film existingFilm = new Film();
        existingFilm.setFilmId(1L);

        when(filmRepository.findByFilmId(1L))
                .thenReturn(Optional.of(existingFilm));

        filmService.saveNewFilms(List.of(filmDto));

        verify(filmRepository, never()).save(any());

    }

    @Test
    void test_saveNewFilms_whenFilmNotExists_shouldSave(){
        FilmDto filmDto = new FilmDto();
        filmDto.setFilmId(1L);


        FilmDto apiResponse = new FilmDto();
        apiResponse.setFilmId(1L);
        apiResponse.setFilmName("Test");

        Film mappedFilm = new Film();

        when(filmRepository.findByFilmId(1L)).thenReturn(Optional.empty());
        when(kinopoiskApiService.getFilmDetails(1L)).thenReturn(apiResponse);
        when(filmMapper.DtoToFilm(apiResponse)).thenReturn(mappedFilm);

        filmService.saveNewFilms(List.of(filmDto));
        verify(filmRepository, times(1)).save(mappedFilm);
    }

    @Test
    void test_saveNewFilms_shouldProcessList(){
        FilmDto first =  new FilmDto();
        first.setFilmId(1L);

        FilmDto second =  new FilmDto();
        second.setFilmId(2L);

        when(filmRepository.findByFilmId(1L)).thenReturn(Optional.empty());
        when(filmRepository.findByFilmId(2L)).thenReturn(Optional.empty());

        when(kinopoiskApiService.getFilmDetails(anyLong())).thenReturn(new FilmDto());
        when(filmMapper.DtoToFilm(any())).thenReturn(new Film());

        filmService.saveNewFilms(List.of(first,second));

        verify(filmRepository, times(2)).save(any(Film.class));
    }

    @Test
    void test_searchFilms_shouldCallDao() {
        Page<Film> mockPage = mock(Page.class);

        when(filmDao.findByFilters(
                eq("Avatar"), eq(2009),
                eq(7.0), eq(9.0),
                eq(0), eq(10),
                eq("rating"), eq("desc")
        )).thenReturn(mockPage);

        Page<Film> result = filmService.searchFilms(
                "Avatar", 2009, 7.0, 9.0,
                0, 10, "rating", "desc"
        );

        assertEquals(mockPage, result);
    }

    @Test
    void test_getAllFilms_shouldCallRepository() {
        filmService.getAllFilms();
        verify(filmRepository, times(1)).findAll();
    }

    @Test
    void test_clearAllFilms_shouldDeleteAll() {
        filmService.clearAllFilms();
        verify(filmRepository, times(1)).deleteAll();
    }
}
