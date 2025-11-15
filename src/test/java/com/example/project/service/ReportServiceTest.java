package com.example.project.service;


import com.example.project.model.entity.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    private ReportService reportService;

    @BeforeEach
    public void setup() {
        reportService = new ReportService();
    }

    @Test
    void test_generateCsv_Success() {
        Film f1 = new Film();
        f1.setId(1L);
        f1.setFilmId(101L);
        f1.setFilmName("Film 1");
        f1.setYear(2010);
        f1.setRating(8.8);
        f1.setDescription("First Film");

        Film f2 = new Film();
        f2.setId(2L);
        f2.setFilmId(202L);
        f2.setFilmName("Second Film");
        f2.setYear(null);
        f2.setRating(null);
        f2.setDescription(null);

        byte[] csvBytes = reportService.generateCsv(List.of(f1, f2));
        String csv = new String(csvBytes, StandardCharsets.UTF_8);

        String[] lines = csv.split("\n");

        assertThat(lines.length).isEqualTo(3);

        // OpenCSV добавляет кавычки вокруг всех полей
        assertThat(lines[0])
                .isEqualTo("\"ID\",\"FilmId\",\"Name\",\"Year\",\"Rating\",\"Description\"");

        assertThat(lines[1])
                .isEqualTo("\"1\",\"101\",\"Film 1\",\"2010\",\"8.8\",\"First Film\"");

        assertThat(lines[2])
                .isEqualTo("\"2\",\"202\",\"Second Film\",\"\",\"\",\"\"");
    }

    @Test
    void test_generateXml_Success() {
        Film f1 = new Film();
        f1.setId(1L);
        f1.setFilmId(101L);
        f1.setFilmName("Film 1");
        f1.setYear(2010);
        f1.setRating(8.8);
        f1.setDescription("First Film");

        Film f2 = new Film();
        f2.setId(2L);
        f2.setFilmId(202L);
        f2.setFilmName("Second Film");
        f2.setYear(null);
        f2.setRating(null);
        f2.setDescription(null);

        byte[] xmlBytes = reportService.generateXml(List.of(f1, f2));
        String xml = new String(xmlBytes, StandardCharsets.UTF_8);


        assertThat(xml).contains("<Film>");
        assertThat(xml).contains("<id>1</id>");
        assertThat(xml).contains("<filmId>101</filmId>");
        assertThat(xml).contains("<filmName>Film 1</filmName>");
        assertThat(xml).contains("<year>2010</year>");
        assertThat(xml).contains("<rating>8.8</rating>");
        assertThat(xml).contains("<description>First Film</description>");

        assertThat(xml).contains("<id>2</id>");
        assertThat(xml).contains("<filmId>202</filmId>");
        assertThat(xml).contains("<filmName>Second Film</filmName>");
        assertThat(xml).contains("<year/>");
        assertThat(xml).contains("<rating/>");
        assertThat(xml).contains("<description/>");
    }
}
