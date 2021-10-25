package com.itechart.book_library.service;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.api.AuthorDao;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.api.GenreDao;
import com.itechart.book_library.dao.criteria.BookSpecification;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.GenreEntity;
import com.itechart.book_library.util.converter.Converter;
import com.itechart.book_library.util.converter.impl.BookConverter;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j
public enum BookService {
    INSTANCE;

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final BookDao bookDao = BaseDao.getDao(BookDaoImpl.class);
    private final AuthorDao authorDao = BaseDao.getDao(AuthorDaoImpl.class);
    private final GenreDao genreDao = BaseDao.getDao(GenreDaoImpl.class);
    private final AuthorBookDaoImpl authorBookDao = BaseDao.getDao(AuthorBookDaoImpl.class);
    private final GenreBookDaoImpl genreBookDao = BaseDao.getDao(GenreBookDaoImpl.class);
    private final Converter<BookDto, BookEntity> bookConverter = new BookConverter();

    public void create(BookDto bookDto) {

        BookEntity bookEntity = bookConverter.toEntity(bookDto);
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        try {
            bookDao.create(bookEntity, connection);
            manageAuthorsAndGenres(bookEntity, connection);
            commit(connection);
        } catch (SQLException e) {
            rollback(connection);
        }

        setAutoCommit(connection, true);
    }

    public void update(BookDto bookDto) {
        if (bookDao.getById(bookDto.getId()).isEmpty()) {
            create(bookDto);
            return;
        }
        BookEntity book = bookConverter.toEntity(bookDto);
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);
        try {
            bookDao.update(book, connection);
            manageAuthorsAndGenres(book, connection);
            commit(connection);
        } catch (SQLException e) {
            rollback(connection);
        }
        setAutoCommit(connection, true);
    }

    public List<BookDto> getLimitOffsetBySpecification(BookSpecification specification, int bookAmountOnOnePage, int page) {
        int offset = (page - 1) * bookAmountOnOnePage;
        return bookConverter.toDtos(bookDao.getLimitOffsetBySpecification(specification, bookAmountOnOnePage, offset));
    }

    public BookDto getById(int id) {
        Optional<BookEntity> optionalBook = bookDao.getById(id);
        if (optionalBook.isEmpty()) return null;
        return bookConverter.toDto(optionalBook.get());
    }

    private void manageAuthorsAndGenres(BookEntity bookEntity, Connection connection) throws SQLException {
        for (AuthorEntity authorEntity : bookEntity.getAuthorEntities()) {
            manageAuthor(bookEntity, connection, authorEntity);
        }
        for (GenreEntity genreEntity : bookEntity.getGenreEntities()) {
            manageGenre(bookEntity, connection, genreEntity);
        }
    }

    private void manageAuthor(BookEntity bookEntity, Connection connection, AuthorEntity authorEntity) throws SQLException {
        Optional<AuthorEntity> optionalAuthor = authorDao.getByName(authorEntity.getName());
        if (optionalAuthor.isEmpty()) {
            authorEntity = authorDao.create(authorEntity, connection);
            authorBookDao.setAuthorToBook(authorEntity.getId(), bookEntity.getId(), connection);
        } else {
            authorBookDao.setAuthorToBook(optionalAuthor.get().getId(), bookEntity.getId(), connection);
        }
    }

    private void manageGenre(BookEntity bookEntity, Connection connection, GenreEntity genreEntity) throws SQLException {
        Optional<GenreEntity> optionalGenre = genreDao.getByName(genreEntity.getName());
        if (optionalGenre.isEmpty()) {
            genreEntity = genreDao.create(genreEntity, connection);
            genreBookDao.setGenreToBook(genreEntity.getId(), bookEntity.getId(), connection);
        } else {
            genreBookDao.setGenreToBook(optionalGenre.get().getId(), bookEntity.getId(), connection);
        }
    }

    private void setAutoCommit(Connection connection, boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            log.error(e);
        }
    }

    private void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    public void delete(String[] ids) {
        bookDao.delete(Arrays.stream(ids).map(Integer::parseInt).toArray(Integer[]::new));
    }

    public int getBookCountBySpecification(BookSpecification specification) {
        return bookDao.getCountBySpecification(specification);
    }

}
