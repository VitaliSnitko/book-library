package com.itechart.book_library.service;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.api.AuthorDao;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.api.GenreDao;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.dao.criteria.BookSpecification;
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
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookService {

    private static final Logger log = Logger.getLogger(BookDaoImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final BookDao bookDao = BaseDao.getDao(BookDaoImpl.class);
    private final AuthorDao authorDao = BaseDao.getDao(AuthorDaoImpl.class);
    private final GenreDao genreDao = BaseDao.getDao(GenreDaoImpl.class);
    private final AuthorBookDaoImpl authorBookDao = BaseDao.getDao(AuthorBookDaoImpl.class);
    private final GenreBookDaoImpl genreBookDao = BaseDao.getDao(GenreBookDaoImpl.class);
    private final Converter<BookDto, BookEntity> bookConverter = new BookConverter();
    private final Converter<AuthorDto, AuthorEntity> authorConverter = new AuthorConverter();
    private final Converter<GenreDto, GenreEntity> genreConverter = new GenreConverter();

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

    public List<BookDto> getLimitOffsetBySpecification(BookSpecification specification, int bookAmountOnOnePage, int page) {
        int offset = (page - 1) * bookAmountOnOnePage;
        List<BookEntity> bookEntityList = bookDao.getLimitOffsetBySpecification(specification, bookAmountOnOnePage, offset);
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

        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);

        bookDao.update(book, connection);
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
        commit(connection);
        setAutoCommit(connection, true);
    }

    private void setAutoCommit(Connection connection, boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            log.error("Cannot set autoCommit", e);
        }
    }

    private void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    public void delete(String[] ids) {
        bookDao.delete(Arrays.stream(ids).map(Integer::parseInt).toArray(Integer[]::new));
    }

    public int getBookCount() {
        return bookDao.getCount();
    }

    public int getBookCountBySpecification(BookSpecification specification) {
        return bookDao.getCountBySpecification(specification);
    }

}
