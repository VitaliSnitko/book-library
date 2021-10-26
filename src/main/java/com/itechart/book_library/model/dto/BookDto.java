package com.itechart.book_library.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

@Builder
@Getter
public class BookDto {

    private int id;
    private String title;
    List<AuthorDto> authorDtos;
    List<GenreDto> genreDtos;
    private String publisher;
    private Date publishDate;
    private int pageCount;
    private String ISBN;
    private String description;
    private InputStream cover;
    private String base64Cover;
    private int availableBookAmount;
    private int totalBookAmount;

    public BookDto(int id, String title, List<AuthorDto> authorDtos, List<GenreDto> genreDtos,
                   String publisher, Date publishDate, int pageCount, String ISBN, String description,
                   InputStream cover, int availableBookAmount, int totalBookAmount) {
        this.id = id;
        this.title = title;
        this.authorDtos = authorDtos;
        this.genreDtos = genreDtos;

        this.publisher = publisher;
        this.publishDate = publishDate;
        this.pageCount = pageCount;
        this.ISBN = ISBN;
        this.description = description;
        this.cover = cover;
        try {
            base64Cover = (cover == null) ? "" : Base64.getEncoder().encodeToString(IOUtils.toByteArray(cover));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.availableBookAmount = availableBookAmount;
        this.totalBookAmount = totalBookAmount;
    }
}
