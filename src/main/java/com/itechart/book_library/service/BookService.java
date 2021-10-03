package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.*;
import com.itechart.book_library.dao.exception.DaoException;
import com.itechart.book_library.dao.impl.AuthorDaoImpl;
import com.itechart.book_library.dao.impl.BookDaoImpl;
import com.itechart.book_library.dao.impl.GenreDaoImpl;
import com.itechart.book_library.entity.Author;
import com.itechart.book_library.entity.Book;
import com.itechart.book_library.entity.Genre;

import java.util.List;

public class BookService {

    public Book createBook(Book book, String[] authors, String[] genres) {
        BookDao bookDao = getDao(BookDaoImpl.class);
        bookDao.create(book);
        int bookId = bookDao.getByTitle(book.getTitle()).getId();
        for (String authorName : authors) {
            AuthorDao authorDao = getDao(AuthorDaoImpl.class);
            Author author = authorDao.create(new Author(authorName));
            int authorId = authorDao.getByName(author.getName()).getId();
            bookDao.setAuthorToBook(authorId, bookId);
        }
        for (String genreName : genres) {
            GenreDao genreDao = getDao(GenreDaoImpl.class);
            Genre genre = genreDao.create(new Genre(genreName));
            int genreId = genreDao.getByName(genre.getName()).getId();
            bookDao.setGenreToBook(genreId, bookId);
        }
        return getDao(BookDaoImpl.class).create(book);
    }

    public List<Book> getListOfBooks() {
        return getDao(BookDaoImpl.class).getAll();
    }

    public Book getBookById(int id) {
        return getDao(BookDaoImpl.class).getById(id);
    }

    public void updateBook(Book book) {
        try (DaoFactory daoFactory = new DaoFactory()) {
            try {
                BookDao bookDao = daoFactory.getDao(BookDaoImpl.class);
                daoFactory.startTransaction();
                bookDao.update(book);
                daoFactory.commitTransaction();
            } catch (DaoException e) {
                daoFactory.rollbackTransaction();
                e.printStackTrace();
            }
        }
    }

    public void deleteBook(int id) {
        getDao(BookDaoImpl.class).delete(id);
    }

    public Book getBookByTitle(String title) {
        return getDao(BookDaoImpl.class).getByTitle(title);
    }

    public List<Book> getBooksByDescription(String description) {
        return getDao(BookDaoImpl.class).getByDescription(description);
    }

    public List<Book> getBooksByAuthor(int id) {
        return getDao(BookDaoImpl.class).getByAuthor(id);
    }

    public List<Book> getBooksByGenre(int id) {
        return getDao(BookDaoImpl.class).getByGenre(id);
    }


    public void setAuthorToBook(int authorId, int bookId) {
        getDao(BookDaoImpl.class).setAuthorToBook(authorId, bookId);
    }

    public void setGenreToBook(int genreId, int bookId) {
        getDao(BookDaoImpl.class).setGenreToBook(genreId, bookId);
    }

    private <T extends BaseDao> T getDao(Class<T> clazz) {
        try {
            return getDaoFactory().getDao(clazz);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DaoFactory getDaoFactory() {
        try (DaoFactory daoFactory = new DaoFactory()) {
            return daoFactory;
        }
    }
}
