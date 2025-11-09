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

    /**
     * Отправляет отчёт на email в формате CSV или XML
     * Пример запроса:
     * GET /api/v2/reports/send?email=test@example.com&format=csv
     */
    @GetMapping("/send")
    public ResponseEntity<String> sendReport(
            @RequestParam String email,
            @RequestParam(defaultValue = "csv") String format
    ) {
        // 1. Получаем все фильмы
        List<Film> films = filmService.getAllFilms();

        // 2. Генерируем отчёт в нужном формате
        byte[] report = "xml".equalsIgnoreCase(format)
                ? reportService.generateXml(films)
                : reportService.generateCsv(films);

        // 3. Отправляем письмо
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
