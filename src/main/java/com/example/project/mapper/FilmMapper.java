package com.example.project.mapper;


import com.example.project.model.dto.FilmDto;
import com.example.project.model.entity.Film;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FilmMapper {

    @Mapping(target = "filmId", source = "filmId")
    @Mapping(target = "filmName", source = "filmName")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "description", source = "description")
    FilmDto FilmToDto (Film film);

    @Mapping(target = "filmId", source = "filmId")
    @Mapping(target = "filmName", source = "filmName")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "description", source = "description")
    Film DtoToFilm (FilmDto filmDto);
}
