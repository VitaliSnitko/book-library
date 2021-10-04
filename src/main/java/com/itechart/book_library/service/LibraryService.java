package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.*;
import com.itechart.book_library.dao.exception.DaoException;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.entity.Author;
import com.itechart.book_library.model.entity.Book;
import com.itechart.book_library.model.entity.Genre;

import java.util.List;

public class LibraryService {

    private static LibraryService libraryService;

    private LibraryService() {}

    public static LibraryService getInstance() {
        if (libraryService == null)
            libraryService = new LibraryService();
        return libraryService;
    }

    public void createBook(BookDto bookDto) throws DaoException {

        BookDao bookDao = DaoFactory.getInstance().getDao(BookDaoImpl.class);
        AuthorDao authorDao = DaoFactory.getInstance().getDao(AuthorDaoImpl.class);
        GenreDao genreDao = DaoFactory.getInstance().getDao(GenreDaoImpl.class);
        AuthorBookDaoImpl authorBookDao = DaoFactory.getInstance().getDao(AuthorBookDaoImpl.class);
        GenreBookDaoImpl genreBookDao = DaoFactory.getInstance().getDao(GenreBookDaoImpl.class);

        Book book = bookDto.convertBookDtoToBookEntity();
        List<Author> listOfAuthors = bookDto.convertBookDtoToListOfAuthors();
        List<Genre> listOfGenres = bookDto.convertBookDtoToListOfGenres();

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

}
