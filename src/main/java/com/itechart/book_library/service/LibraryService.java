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

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {

    private static BookDao bookDao = getDao(BookDaoImpl.class);
    private static AuthorDao authorDao = getDao(AuthorDaoImpl.class);
    private static GenreDao genreDao = getDao(GenreDaoImpl.class);
    private static AuthorBookDaoImpl authorBookDao = getDao(AuthorBookDaoImpl.class);
    private static GenreBookDaoImpl GENRE_BOOK_DAO = getDao(GenreBookDaoImpl.class);

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

        BookEntity book = BOOK_CONVERTER.toEntity(bookDto);
        List<AuthorEntity> authorEntityList = AUTHOR_CONVERTER.toEntities(bookDto.getAuthorDtos());
        List<GenreEntity> genreEntityList = GENRE_CONVERTER.toEntities(bookDto.getGenreDtos());

        bookDao.create(book);
        for (AuthorEntity authorEntity : authorEntityList) {
            authorEntity = authorDao.create(authorEntity);
            authorBookDao.setAuthorToBook(authorEntity.getId(), book.getId());
        }
        for (GenreEntity genreEntity : genreEntityList) {
            genreEntity = genreDao.create(genreEntity);
            GENRE_BOOK_DAO.setGenreToBook(genreEntity.getId(), book.getId());
        }
    }

    public List<BookDto> getAll() throws SQLException {

        List<BookDto> bookDtoList = new ArrayList<>();
        List<BookEntity> bookEntityList = bookDao.getAll();

        for (BookEntity bookEntity : bookEntityList) {
            List<AuthorEntity> authorEntityListAssignedToBook = authorDao.getByBookId(bookEntity.getId());
            List<GenreEntity> genreEntityListAssignedToBook = genreDao.getByBookId(bookEntity.getId());
            BookDto bookDto = BOOK_CONVERTER.toDto(bookEntity);
            bookDto.setAuthorDtos(AUTHOR_CONVERTER.toDtos(authorEntityListAssignedToBook));
            bookDto.setGenreDtos(GENRE_CONVERTER.toDtos(genreEntityListAssignedToBook));
            bookDtoList.add(bookDto);
        }

        return bookDtoList;
    }

    public BookDto getById(int id) throws SQLException {
        Optional<BookEntity> optionalBook = bookDao.getById(id);
        if (optionalBook.isEmpty()) return null;

        BookEntity bookEntity = optionalBook.get();
        List<AuthorEntity> authorEntityList = authorDao.getByBookId(bookEntity.getId());
        List<GenreEntity> genreEntityList = genreDao.getByBookId(bookEntity.getId());
        BookDto bookDto = BOOK_CONVERTER.toDto(bookEntity);
        bookDto.setAuthorDtos(AUTHOR_CONVERTER.toDtos(authorEntityList));
        bookDto.setGenreDtos(GENRE_CONVERTER.toDtos(genreEntityList));
        return bookDto;
    }

    public void update(BookDto bookDto) throws SQLException {
        BookEntity book = BOOK_CONVERTER.toEntity(bookDto);
        List<AuthorEntity> authorEntityList = AUTHOR_CONVERTER.toEntities(bookDto.getAuthorDtos());
        List<GenreEntity> genreEntityList = GENRE_CONVERTER.toEntities(bookDto.getGenreDtos());

        bookDao.update(book);
        for (AuthorEntity authorEntity : authorEntityList) {
            if (authorDao.getByName(authorEntity.getName()).isEmpty()) {
                authorEntity = authorDao.create(authorEntity);
                authorBookDao.setAuthorToBook(authorEntity.getId(), book.getId());
            }
        }
        for (GenreEntity genreEntity : genreEntityList) {
            if (genreDao.getByName(genreEntity.getName()).isEmpty()) {
                genreEntity = genreDao.create(genreEntity);
                GENRE_BOOK_DAO.setGenreToBook(genreEntity.getId(), book.getId());
            }
        }
    }
    
    public static <T extends BaseDao> T getDao(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}
