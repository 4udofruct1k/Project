package com.example.project.service;

import com.example.project.model.entity.Film;
import com.opencsv.CSVWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
public class ReportService {

    public byte[] generateCsv(List<Film> films) {
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeNext(new String[]{"ID", "FilmId", "Name", "Year", "Rating", "Description"});
            for (Film f : films) {
                csvWriter.writeNext(new String[]{
                        f.getId().toString(),
                        f.getFilmId().toString(),
                        f.getFilmName(),
                        f.getYear() != null ? f.getYear().toString() : "",
                        f.getRating() != null ? f.getRating().toString() : "",
                        f.getDescription() != null ? f.getDescription() : ""
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации CSV", e);
        }
        return writer.toString().getBytes();
    }

    public byte[] generateXml(List<Film> films) {
        try {
            XmlMapper mapper = new XmlMapper();
            return mapper.writeValueAsBytes(films);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации XML", e);
        }
    }
}
