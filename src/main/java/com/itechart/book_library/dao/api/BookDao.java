package com.itechart.book_library.dao.api;

import com.itechart.book_library.entity.Book;

import java.util.List;

public interface BookDao extends Dao<Book>{
    public Book getByTitle(String title);
    public List<Book> getByAuthor(int id);
    public List<Book> getByGenre(int id);
    public List<Book> getByDescription(String description);
    public void setAuthorToBook(int authorId, int bookId);
    public void setGenreToBook(int genreId, int bookId);
}
