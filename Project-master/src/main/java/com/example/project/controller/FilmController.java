package com.example.project.controller;

import com.example.project.model.dto.KinopoiskResponse;
import com.example.project.model.entity.Film;
import com.example.project.service.EmailService;
import com.example.project.service.FilmService;
import com.example.project.service.KinopoiskApiService;
import com.example.project.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/films")
@RequiredArgsConstructor
public class FilmController {

    private final KinopoiskApiService kinopoiskApiService;
    private final FilmService filmService;
    private final ReportService reportService;
    private final EmailService emailService;

    @GetMapping("/load")
    public ResponseEntity<KinopoiskResponse> loadFilmsFromKinopoisk(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page
    ) {
        KinopoiskResponse response = kinopoiskApiService.searchFilms(keyword, page);
        filmService.saveNewFilms(response.getItems()); // сохраняем только новые
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<Film>> searchFilms(
            @RequestParam(required = false) String filmName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Film> films = filmService.searchFilms(filmName, year, minRating, maxRating, page, size, sortBy, direction);
        return ResponseEntity.ok(films);
    }

    @GetMapping("/report")
    public ResponseEntity<String> sendReport(
            @RequestParam String email,
            @RequestParam(defaultValue = "csv") String format
    ) {
        List<Film> films = filmService.getAllFilms();
        byte[] report = "xml".equalsIgnoreCase(format)
                ? reportService.generateXml(films)
                : reportService.generateCsv(films);

        emailService.sendReport(
                email,
                "Отчёт по фильмам",
                "Во вложении файл с отчётом.",
                report,
                "films_report." + format
        );

        return ResponseEntity.ok("Отчёт отправлен на " + email);
    }


    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllFilms() {
        filmService.clearAllFilms();
        return ResponseEntity.ok("База фильмов очищена успешно!");
    }

}