package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.AuthorDto;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.GenreDto;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.util.converter.api.AuthorConverter;
import com.itechart.book_library.util.converter.api.BookConverter;
import com.itechart.book_library.util.converter.api.GenreConverter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;


public class BookConverterImpl implements BookConverter {

    private final AuthorConverter authorConverter = new AuthorConverterImpl();
    private final GenreConverter genreConverter = new GenreConverterImpl();

    @Override
    public BookEntity toEntity(BookDto bookDto) {
        return BookEntity.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .authorEntities(authorConverter.toEntities(bookDto.getAuthorDtos()))
                .genreEntities(genreConverter.toEntities(bookDto.getGenreDtos()))
                .publisher(bookDto.getPublisher())
                .publishDate(Date.valueOf(bookDto.getPublishDate()))
                .pageCount(bookDto.getPageCount())
                .ISBN(bookDto.getISBN())
                .description(bookDto.getDescription())
                .cover(bookDto.getCover())
                .availableBookAmount(bookDto.getAvailableBookAmount())
                .totalBookAmount(bookDto.getTotalBookAmount())
                .build();
    }

    @SneakyThrows
    @Override
    public BookDto toDto(BookEntity bookEntity) {
        InputStream cover = bookEntity.getCover();
        String base64Cover = (cover == null) ? "" : Base64.getEncoder().encodeToString(IOUtils.toByteArray(cover));
        return BookDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .authorDtos(authorConverter.toDtos(bookEntity.getAuthorEntities()))
                .genreDtos(genreConverter.toDtos(bookEntity.getGenreEntities()))
                .publisher(bookEntity.getPublisher())
                .publishDate(bookEntity.getPublishDate().toLocalDate())
                .pageCount(bookEntity.getPageCount())
                .ISBN(bookEntity.getISBN())
                .description(bookEntity.getDescription())
                .cover(cover)
                .base64Cover(base64Cover)
                .availableBookAmount(bookEntity.getAvailableBookAmount())
                .totalBookAmount(bookEntity.getTotalBookAmount())
                .build();
    }

    @SneakyThrows
    @Override
    public BookDto toDtoFromReq(HttpServletRequest req) {
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
                .publishDate(LocalDate.parse(req.getParameter("date")))
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
}
