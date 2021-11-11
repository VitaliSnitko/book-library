package com.itechart.library.converter.impl;

import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.Converter;
import com.itechart.library.model.dto.AuthorDto;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.dto.GenreDto;
import com.itechart.library.model.entity.AuthorEntity;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.GenreEntity;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class BookConverterImpl implements BookConverter {

    private final Converter<AuthorDto, AuthorEntity> authorConverter = new AuthorConverterImpl();
    private final Converter<GenreDto, GenreEntity> genreConverter = new GenreConverterImpl();

    @Override
    public BookEntity toEntity(BookDto bookDto) {
        return BookEntity.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .authorEntities(bookDto.getAuthorDtos() == null ? new ArrayList<>() : authorConverter.toEntities(bookDto.getAuthorDtos()))
                .genreEntities(bookDto.getGenreDtos() == null ? new ArrayList<>() : genreConverter.toEntities(bookDto.getGenreDtos()))
                .publisher(bookDto.getPublisher())
                .publishDate(bookDto.getPublishDate() == null ? null : Date.valueOf(bookDto.getPublishDate()))
                .pageCount(bookDto.getPageCount())
                .ISBN(bookDto.getISBN())
                .description(bookDto.getDescription())
                .cover(bookDto.getCover())
                .availableBookAmount(bookDto.getAvailableBookAmount())
                .totalBookAmount(bookDto.getTotalBookAmount())
                .build();
    }

    @Override
    public BookDto toDto(BookEntity bookEntity) {
        List<AuthorEntity> authorEntities = bookEntity.getAuthorEntities();
        List<GenreEntity> genreEntities = bookEntity.getGenreEntities();
        InputStream cover = bookEntity.getCover();
        String base64Cover = getBase64Cover(cover);
        Date publishDate = bookEntity.getPublishDate();
        return BookDto.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .authorDtos(authorEntities == null ? new ArrayList<>() : authorConverter.toDtos(authorEntities))
                .genreDtos(genreEntities == null ? new ArrayList<>() : genreConverter.toDtos(genreEntities))
                .publisher(bookEntity.getPublisher())
                .publishDate(publishDate == null ? null : publishDate.toLocalDate())
                .pageCount(bookEntity.getPageCount())
                .ISBN(bookEntity.getISBN())
                .description(bookEntity.getDescription())
                .cover(cover)
                .base64Cover(base64Cover)
                .availableBookAmount(bookEntity.getAvailableBookAmount())
                .totalBookAmount(bookEntity.getTotalBookAmount())
                .build();
    }

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
                .cover(getCover(req))
                .availableBookAmount(totalBookAmount)
                .totalBookAmount(totalBookAmount)
                .build();
    }

    private String getBase64Cover(InputStream cover) {
        try {
            return (cover == null) ? "" : Base64.getEncoder().encodeToString(IOUtils.toByteArray(cover));
        } catch (IOException e) {
            log.error(e);
        }
        return "";
    }

    private InputStream getCover(HttpServletRequest req) {
        try {
            return req.getPart("cover").getInputStream().available() == 0 ?
                    null :
                    req.getPart("cover").getInputStream();
        } catch (IOException | ServletException e) {
            log.error(e);
        }
        return null;
    }
}
