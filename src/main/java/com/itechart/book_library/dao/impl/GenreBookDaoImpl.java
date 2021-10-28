package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenreBookDaoImpl extends BaseDao {

    private static final String INSERT_GENRE_BOOK_QUERY = "INSERT INTO genre_book (genre_id, book_id) VALUES (?,?)";
    private static final String DELETE_GENRE_BOOK_QUERY = """
            DELETE
            from genre_book
            where book_id in (
                select book_id
                from genre_book
                         join genre on genre.id = genre_book.genre_id
                         join book on book.id = genre_book.book_id
                where book_id = ?
            )""";

    public void setGenreToBook(int authorId, int bookId, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_GENRE_BOOK_QUERY)) {
            statement.setInt(1, authorId);
            statement.setInt(2, bookId);
            statement.execute();
        }
    }

    public void removeGenresFromBook(int bookId, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_GENRE_BOOK_QUERY)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }
}
