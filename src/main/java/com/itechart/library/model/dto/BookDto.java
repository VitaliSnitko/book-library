package com.itechart.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookDto extends BaseDto {

    private String title;
    private List<AuthorDto> authorDtos;
    private List<GenreDto> genreDtos;
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
