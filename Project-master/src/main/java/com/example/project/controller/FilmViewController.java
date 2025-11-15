package com.example.project.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FilmViewController {

    @GetMapping("/films")
    public String showFilmsPage() {
        return "films";
    }
}
