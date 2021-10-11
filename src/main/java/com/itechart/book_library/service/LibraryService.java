package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.*;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.model.dto.AuthorDto;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.GenreDto;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.GenreEntity;
import com.itechart.book_library.util.converter.impl.AuthorConverter;
import com.itechart.book_library.util.converter.impl.BookConverter;
import com.itechart.book_library.util.converter.Converter;
import com.itechart.book_library.util.converter.impl.GenreConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {

    private static final BookDao BOOK_DAO = getDaoFactory().getDao(BookDaoImpl.class);
    private static final AuthorDao AUTHOR_DAO = getDaoFactory().getDao(AuthorDaoImpl.class);
    private static final GenreDao GENRE_DAO = getDaoFactory().getDao(GenreDaoImpl.class);
    private static final AuthorBookDaoImpl AUTHOR_BOOK_DAO = getDaoFactory().getDao(AuthorBookDaoImpl.class);
    private static final GenreBookDaoImpl GENRE_BOOK_DAO = getDaoFactory().getDao(GenreBookDaoImpl.class);
    private static final Converter<BookDto, BookEntity> BOOK_CONVERTER = new BookConverter();
    private static final Converter<AuthorDto, AuthorEntity> AUTHOR_CONVERTER = new AuthorConverter();
    private static final Converter<GenreDto, GenreEntity> GENRE_CONVERTER = new GenreConverter();
    private static LibraryService libraryService;

    private LibraryService() {
    }

    public static LibraryService getInstance() {
        if (libraryService == null)
            libraryService = new LibraryService();
        return libraryService;
    }

    public void createBook(BookDto bookDto) throws SQLException {

        BookEntity book = BOOK_CONVERTER.convertFromDto(bookDto);
        List<AuthorEntity> authorEntityList = AUTHOR_CONVERTER.createFromDtos(bookDto.getAuthorDtos());
        List<GenreEntity> genreEntityList = GENRE_CONVERTER.createFromDtos(bookDto.getGenreDtos());

        BOOK_DAO.create(book);
        for (AuthorEntity authorEntity : authorEntityList) {
            authorEntity = AUTHOR_DAO.create(authorEntity);
            AUTHOR_BOOK_DAO.setAuthorToBook(authorEntity.getId(), book.getId());
        }
        for (GenreEntity genreEntity : genreEntityList) {
            genreEntity = GENRE_DAO.create(genreEntity);
            GENRE_BOOK_DAO.setGenreToBook(genreEntity.getId(), book.getId());
        }
    }

    public List<BookDto> getAll() throws SQLException {

        List<BookDto> bookDtoList = new ArrayList<>();
        List<BookEntity> bookEntityList = BOOK_DAO.getAll();

        for (BookEntity bookEntity : bookEntityList) {
            List<AuthorEntity> authorEntityListAssignedToBook = AUTHOR_DAO.getByBookId(bookEntity.getId());
            List<GenreEntity> genreEntityListAssignedToBook = GENRE_DAO.getByBookId(bookEntity.getId());
            BookDto bookDto = BOOK_CONVERTER.convertFromEntity(bookEntity);
            bookDto.setAuthorDtos(AUTHOR_CONVERTER.createFromEntities(authorEntityListAssignedToBook));
            bookDto.setGenreDtos(GENRE_CONVERTER.createFromEntities(genreEntityListAssignedToBook));
            bookDtoList.add(bookDto);
        }

        return bookDtoList;
    }

    public BookDto getById(int id) throws SQLException {
        Optional<BookEntity> optionalBook = BOOK_DAO.getById(id);
        if (optionalBook.isEmpty()) return null;

        BookEntity bookEntity = optionalBook.get();
        List<AuthorEntity> authorEntityList = AUTHOR_DAO.getByBookId(bookEntity.getId());
        List<GenreEntity> genreEntityList = GENRE_DAO.getByBookId(bookEntity.getId());
        BookDto bookDto = BOOK_CONVERTER.convertFromEntity(bookEntity);
        bookDto.setAuthorDtos(AUTHOR_CONVERTER.createFromEntities(authorEntityList));
        bookDto.setGenreDtos(GENRE_CONVERTER.createFromEntities(genreEntityList));
        return bookDto;
    }

    public void update(BookDto bookDto) throws SQLException {
        BookEntity book = BOOK_CONVERTER.convertFromDto(bookDto);
        List<AuthorEntity> authorEntityList = AUTHOR_CONVERTER.createFromDtos(bookDto.getAuthorDtos());
        List<GenreEntity> genreEntityList = GENRE_CONVERTER.createFromDtos(bookDto.getGenreDtos());

        BOOK_DAO.update(book);
        for (AuthorEntity authorEntity : authorEntityList) {
            if (AUTHOR_DAO.getByName(authorEntity.getName()).isEmpty()) {
                authorEntity = AUTHOR_DAO.create(authorEntity);
                AUTHOR_BOOK_DAO.setAuthorToBook(authorEntity.getId(), book.getId());
            }
        }
        for (GenreEntity genreEntity : genreEntityList) {
            if (GENRE_DAO.getByName(genreEntity.getName()).isEmpty()) {
                genreEntity = GENRE_DAO.create(genreEntity);
                GENRE_BOOK_DAO.setGenreToBook(genreEntity.getId(), book.getId());
            }
        }
    }

    private static DaoConnectionProvider getDaoFactory() {
        try (DaoConnectionProvider daoConnectionProvider = new DaoConnectionProvider()) {
            return daoConnectionProvider;
        }
    }

}
