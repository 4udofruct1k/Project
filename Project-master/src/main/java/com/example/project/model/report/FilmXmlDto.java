package com.example.project.model.report;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "film")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilmXmlDto {

    @XmlElement(name = "filmId")
    private Long filmId;

    @XmlElement(name = "filmName")
    private String filmName;

    @XmlElement(name = "year")
    private Integer year;

    @XmlElement(name = "rating")
    private Double rating;

    @XmlElement(name = "description")
    private String description;
}