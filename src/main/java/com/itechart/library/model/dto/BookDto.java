package com.itechart.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class BookDto {

    private int id;
    private String title;
    List<AuthorDto> authorDtos;
    List<GenreDto> genreDtos;
    private String publisher;
    private LocalDate publishDate;
    private int pageCount;
    private String ISBN;
    private String description;
    private InputStream cover;
    private String base64Cover;
    private int availableBookAmount;
    private int totalBookAmount;
}
