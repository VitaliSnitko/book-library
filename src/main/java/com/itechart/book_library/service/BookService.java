package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.*;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.dao.impl.AuthorDaoImpl;
import com.itechart.book_library.dao.impl.BookDaoImpl;
import com.itechart.book_library.dao.impl.GenreDaoImpl;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookService extends Service{

    private static BookDao bookDao = getDao(BookDaoImpl.class);
    private static AuthorDao authorDao = getDao(AuthorDaoImpl.class);
    private static GenreDao genreDao = getDao(GenreDaoImpl.class);
    private static AuthorBookDaoImpl authorBookDao = getDao(AuthorBookDaoImpl.class);
    private static GenreBookDaoImpl genreBookDao = getDao(GenreBookDaoImpl.class);

    private static Converter<BookDto, BookEntity> bookConverter = new BookConverter();
    private static Converter<AuthorDto, AuthorEntity> authorConverter = new AuthorConverter();
    private static Converter<GenreDto, GenreEntity> genreConverter = new GenreConverter();
    private static BookService bookService;

    private BookService() {
    }

    public static BookService getInstance() {
        if (bookService == null)
            bookService = new BookService();
        return bookService;
    }

    public void createBook(BookDto bookDto) {

        BookEntity bookEntity = bookConverter.toEntity(bookDto);

        bookDao.create(bookEntity);
        for (AuthorEntity authorEntity : bookEntity.getAuthorEntities()) {
            authorEntity = authorDao.create(authorEntity);
            authorBookDao.setAuthorToBook(authorEntity.getId(), bookEntity.getId());
        }
        for (GenreEntity genreEntity : bookEntity.getGenreEntities()) {
            genreEntity = genreDao.create(genreEntity);
            genreBookDao.setGenreToBook(genreEntity.getId(), bookEntity.getId());
        }
    }

    public List<BookDto> getLimitOffset(int bookAmountOnOnePage, int page) {
        int offset = (page - 1) * bookAmountOnOnePage;
        List<BookEntity> bookEntityList = bookDao.getLimitOffset(bookAmountOnOnePage, offset);
        return bookConverter.toDtos(bookEntityList);
    }

    public BookDto getById(int id) {
        Optional<BookEntity> optionalBook = bookDao.getById(id);
        if (optionalBook.isEmpty()) return null;
        return bookConverter.toDto(optionalBook.get());
    }

    public void update(BookDto bookDto) {
        BookEntity book = bookConverter.toEntity(bookDto);
        List<AuthorEntity> authorEntityList = authorConverter.toEntities(bookDto.getAuthorDtos());
        List<GenreEntity> genreEntityList = genreConverter.toEntities(bookDto.getGenreDtos());

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
                genreBookDao.setGenreToBook(genreEntity.getId(), book.getId());
            }
        }
    }

    public void delete(String[] ids) {
        bookDao.delete(Arrays.stream(ids).map(Integer::parseInt).toArray(Integer[]::new));
    }

    public int getBookCount() {
        return bookDao.getCount();
    }

}
