package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.*;
import com.itechart.book_library.dao.exception.DaoException;
import com.itechart.book_library.dao.impl.*;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.entity.Author;
import com.itechart.book_library.model.entity.Book;
import com.itechart.book_library.model.entity.Genre;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
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

        BookDao bookDao = getDaoFactory().getDao(BookDaoImpl.class);
        AuthorDao authorDao = getDaoFactory().getDao(AuthorDaoImpl.class);
        GenreDao genreDao = getDaoFactory().getDao(GenreDaoImpl.class);
        AuthorBookDaoImpl authorBookDao = getDaoFactory().getDao(AuthorBookDaoImpl.class);
        GenreBookDaoImpl genreBookDao = getDaoFactory().getDao(GenreBookDaoImpl.class);

        Book book = bookDto.convertBookDtoToBookEntity();
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

    public List<BookDto> getAll() throws DaoException, ServletException, IOException {
        BookDao bookDao = getDaoFactory().getDao(BookDaoImpl.class);
        AuthorDao authorDao = getDaoFactory().getDao(AuthorDaoImpl.class);
        GenreDao genreDao = getDaoFactory().getDao(GenreDaoImpl.class);

        List<BookDto> bookDtoList = new ArrayList<>();
        List<Book> listOfBooks = bookDao.getAll();

        for (Book book : listOfBooks) {
            List<Author> listOfAuthors = authorDao.getByBookId(book.getId());
            List<Genre> listOfGenres = genreDao.getByBookId(book.getId());
            bookDtoList.add(new BookDto(book, listOfAuthors, listOfGenres));
        }

        return bookDtoList;
    }

    private DaoFactory getDaoFactory() {
        try(DaoFactory daoFactory = new DaoFactory()) {
            return daoFactory;
        }
    }

}
