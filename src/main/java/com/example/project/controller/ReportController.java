package com.example.project.controller;

import com.example.project.model.entity.Film;
import com.example.project.service.EmailService;
import com.example.project.service.FilmService;
import com.example.project.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/reports")
@RequiredArgsConstructor
public class ReportController {

    private final FilmService filmService;
    private final ReportService reportService;
    private final EmailService emailService;

    @GetMapping("/send")
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
                "Во вложении находится отчёт в формате " + format.toUpperCase(),
                report,
                "films_report." + format.toLowerCase()
        );

        return ResponseEntity.ok("Отчёт успешно отправлен на " + email);
    }
}
