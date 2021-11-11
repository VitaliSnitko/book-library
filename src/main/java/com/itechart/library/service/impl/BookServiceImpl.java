package com.itechart.library.service.impl;

import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.impl.BookConverterImpl;
import com.itechart.library.dao.AuthorDao;
import com.itechart.library.dao.BookDao;
import com.itechart.library.dao.DaoFactory;
import com.itechart.library.dao.GenreDao;
import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.dao.impl.*;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.entity.AuthorEntity;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.GenreEntity;
import com.itechart.library.service.BookService;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final AuthorBookDaoImpl authorBookDao;
    private final GenreBookDaoImpl genreBookDao;

    private final BookConverter bookConverter = new BookConverterImpl();

    public BookServiceImpl() {
        this.bookDao = DaoFactory.getDao(BookDaoImpl.class);
        this.authorDao = DaoFactory.getDao(AuthorDaoImpl.class);
        this.genreDao = DaoFactory.getDao(GenreDaoImpl.class);
        this.authorBookDao = DaoFactory.getDao(AuthorBookDaoImpl.class);
        this.genreBookDao = DaoFactory.getDao(GenreBookDaoImpl.class);
    }

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, AuthorBookDaoImpl authorBookDao, GenreBookDaoImpl genreBookDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.authorBookDao = authorBookDao;
        this.genreBookDao = genreBookDao;
    }

    @Override
    public void create(BookDto bookDto, Connection connection) throws SQLException {
        BookEntity bookEntity = bookConverter.toEntity(bookDto);
        bookEntity = bookDao.create(bookEntity, connection);
        saveAuthorsAndGenres(bookEntity, connection);
    }

    @Override
    public void update(BookDto bookDto, Connection connection) throws SQLException {
        if (bookDao.getById(bookDto.getId()).isEmpty()) {
            create(bookDto, connection);
            return;
        }
        BookEntity bookEntity = bookConverter.toEntity(bookDto);
        bookEntity.setAvailableBookAmount(bookDao.getById(bookEntity.getId()).get().getAvailableBookAmount());
        bookDao.update(bookEntity, connection);
        saveAuthorsAndGenres(bookEntity, connection);
    }

    @Override
    public List<BookDto> getLimitOffsetBySpecification(BookSpecification specification, int bookAmountOnOnePage, int page) {
        int offset = (page - 1) * bookAmountOnOnePage;
        return bookConverter.toDtos(bookDao.getLimitOffsetBySpecification(specification, bookAmountOnOnePage, offset));
    }

    @Override
    public BookDto getById(int id) {
        Optional<BookEntity> optionalBook = bookDao.getById(id);
        if (optionalBook.isEmpty()) {
            return new BookDto();
        }
        return bookConverter.toDto(optionalBook.get());
    }

    @Override
    public int getBookCount(BookSpecification specification) {
        return bookDao.getCount(specification);
    }

    @Override
    public void delete(String[] ids) {
        bookDao.delete(Arrays.stream(ids).map(Integer::parseInt).toArray(Integer[]::new));
    }

    private void saveAuthorsAndGenres(BookEntity bookEntity, Connection connection) throws SQLException {
        removeAuthorsAndGenresFromBook(bookEntity, connection);
        for (AuthorEntity authorEntity : bookEntity.getAuthorEntities()) {
            saveAuthor(bookEntity, connection, authorEntity);
        }
        for (GenreEntity genreEntity : bookEntity.getGenreEntities()) {
            saveGenre(bookEntity, connection, genreEntity);
        }
    }

    private void removeAuthorsAndGenresFromBook(BookEntity bookEntity, Connection connection) throws SQLException {
        authorBookDao.removeAuthorsFromBook(bookEntity.getId(), connection);
        genreBookDao.removeGenresFromBook(bookEntity.getId(), connection);
    }

    private void saveAuthor(BookEntity bookEntity, Connection connection, AuthorEntity authorEntity) throws SQLException {
        Optional<AuthorEntity> optionalAuthor = authorDao.getByName(authorEntity.getName());
        if (optionalAuthor.isEmpty()) {
            authorEntity = authorDao.create(authorEntity, connection);
            authorBookDao.setAuthorToBook(authorEntity.getId(), bookEntity.getId(), connection);
        } else {
            authorBookDao.setAuthorToBook(optionalAuthor.get().getId(), bookEntity.getId(), connection);
        }
    }

    private void saveGenre(BookEntity bookEntity, Connection connection, GenreEntity genreEntity) throws SQLException {
        Optional<GenreEntity> optionalGenre = genreDao.getByName(genreEntity.getName());
        if (optionalGenre.isEmpty()) {
            genreEntity = genreDao.create(genreEntity, connection);
            genreBookDao.setGenreToBook(genreEntity.getId(), bookEntity.getId(), connection);
        } else {
            genreBookDao.setGenreToBook(optionalGenre.get().getId(), bookEntity.getId(), connection);
        }
    }
}
