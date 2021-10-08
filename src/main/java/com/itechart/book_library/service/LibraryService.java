package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.*;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.entity.Author;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.Genre;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {

    private static LibraryService libraryService;
    private static final BookDao bookDao = getDaoFactory().getDao(BookDaoImpl.class);
    private static final AuthorDao authorDao = getDaoFactory().getDao(AuthorDaoImpl.class);
    private static final GenreDao genreDao = getDaoFactory().getDao(GenreDaoImpl.class);
    private static final AuthorBookDaoImpl authorBookDao = getDaoFactory().getDao(AuthorBookDaoImpl.class);
    private static final GenreBookDaoImpl genreBookDao = getDaoFactory().getDao(GenreBookDaoImpl.class);

    private LibraryService() {
    }

    public static LibraryService getInstance() {
        if (libraryService == null)
            libraryService = new LibraryService();
        return libraryService;
    }

    public void createBook(BookDto bookDto) throws SQLException {

        BookEntity book = bookDto.convertBookDtoToBookEntity();
        List<Author> listOfAuthors = bookDto.getListOfAuthors();
        List<Genre> listOfGenres = bookDto.getListOfGenres();

        bookDao.create(book);
        for (Author author : listOfAuthors) {
            author = authorDao.create(author);
            authorBookDao.setAuthorToBook(author.getId(), book.getId());
        }
        for (Genre genre : listOfGenres) {
            genre = genreDao.create(genre);
            genreBookDao.setGenreToBook(genre.getId(), book.getId());
        }
    }

    public List<BookDto> getAll() throws SQLException {
        List<BookDto> bookDtoList = new ArrayList<>();
        List<BookEntity> listOfBooks = bookDao.getAll();
        for (BookEntity book : listOfBooks) {
            List<Author> listOfAuthors = authorDao.getByBookId(book.getId());
            List<Genre> listOfGenres = genreDao.getByBookId(book.getId());
            bookDtoList.add(new BookDto(book, listOfAuthors, listOfGenres));
        }
        return bookDtoList;
    }

    public BookDto getById(int id) throws SQLException {
        Optional<BookEntity> optionalBook = bookDao.getById(id);
        if (optionalBook.isPresent()) {
            BookEntity bookEntity = optionalBook.get();
            List<Author> listOfAuthors = authorDao.getByBookId(bookEntity.getId());
            List<Genre> listOfGenres = genreDao.getByBookId(bookEntity.getId());
            return new BookDto(bookEntity, listOfAuthors, listOfGenres);
        }
        return null;
    }

    public void update(BookDto bookDto) throws SQLException {
        BookEntity book = bookDto.convertBookDtoToBookEntity();
        List<Author> listOfAuthors = bookDto.getListOfAuthors();
        List<Genre> listOfGenres = bookDto.getListOfGenres();
        bookDao.update(book);
        for (Author author : listOfAuthors) {
            if (authorDao.getByName(author.getName()).isEmpty()) {
                author = authorDao.create(author);
                authorBookDao.setAuthorToBook(author.getId(), book.getId());
            }
        }
        for (Genre genre : listOfGenres) {
            if (genreDao.getByName(genre.getName()).isEmpty()) {
                genre = genreDao.create(genre);
                genreBookDao.setGenreToBook(genre.getId(), book.getId());
            }
        }
    }

    private static DaoConnectionProvider getDaoFactory() {
        try (DaoConnectionProvider daoConnectionProvider = new DaoConnectionProvider()) {
            return daoConnectionProvider;
        }
    }

}
