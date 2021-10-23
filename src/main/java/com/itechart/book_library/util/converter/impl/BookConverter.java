package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.AuthorDto;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.GenreDto;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.util.converter.Converter;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Collectors;


public class BookConverter extends Converter<BookDto, BookEntity> {
    private static AuthorConverter authorConverter = new AuthorConverter();
    private static GenreConverter genreConverter = new GenreConverter();

    public BookConverter() {
        super(BookConverter::convertToEntity, BookConverter::convertToDto, BookConverter::convertToDtoFromReq);
    }

    private static BookDto convertToDto(BookEntity bookEntity) {
        return new BookDto(
                bookEntity.getId(),
                bookEntity.getTitle(),
                authorConverter.toDtos(bookEntity.getAuthorEntities()),
                genreConverter.toDtos(bookEntity.getGenreEntities()),
                bookEntity.getPublisher(),
                bookEntity.getPublishDate(),
                bookEntity.getPageCount(),
                bookEntity.getISBN(),
                bookEntity.getDescription(),
                bookEntity.getCover(),
                bookEntity.getAvailableBookAmount(),
                bookEntity.getTotalBookAmount());

    }

    private static BookEntity convertToEntity(BookDto bookDto) {
        return BookEntity.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .authorEntities(authorConverter.toEntities(bookDto.getAuthorDtos()))
                .genreEntities(genreConverter.toEntities(bookDto.getGenreDtos()))
                .publisher(bookDto.getPublisher())
                .publishDate(bookDto.getPublishDate())
                .pageCount(bookDto.getPageCount())
                .ISBN(bookDto.getISBN())
                .description(bookDto.getDescription())
                .cover(bookDto.getCover())
                .availableBookAmount(bookDto.getAvailableBookAmount())
                .totalBookAmount(bookDto.getTotalBookAmount())
                .build();
    }

    @SneakyThrows
    private static BookDto convertToDtoFromReq(HttpServletRequest req) {

        int totalBookAmount = Integer.parseInt(req.getParameter("totalBookAmount"));
        return BookDto.builder()
                .id((req.getParameter("id") != null) ? Integer.parseInt(req.getParameter("id")) : 0)
                .title(req.getParameter("title"))
                .authorDtos(Arrays.stream(req.getParameter("authors").split(" *, *"))
                        .map(AuthorDto::new)
                        .collect(Collectors.toList()))
                .genreDtos(Arrays.stream(req.getParameter("genres").split(" *, *"))
                        .map(GenreDto::new)
                        .collect(Collectors.toList()))
                .publisher(req.getParameter("publisher"))
                .publishDate(getDate(req.getParameter("date")))
                .pageCount(Integer.parseInt(req.getParameter("pageCount")))
                .ISBN(req.getParameter("ISBN"))
                .description(req.getParameter("description"))
                .cover(req.getPart("cover").getInputStream().available() == 0
                        ? null
                        : req.getPart("cover").getInputStream())
                .availableBookAmount(totalBookAmount)
                .totalBookAmount(totalBookAmount)
                .build();
    }

    private static Date getDate(String stringDate) {
        java.util.Date utilDate = null;
        try {
            utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(utilDate.getTime());
    }
}
