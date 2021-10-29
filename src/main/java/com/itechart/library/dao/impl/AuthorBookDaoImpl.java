package com.itechart.library.dao.impl;

import com.itechart.library.dao.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorBookDaoImpl extends BaseDao {

    private static final String INSERT_AUTHOR_BOOK_QUERY = "INSERT INTO author_book (author_id, book_id) VALUES (?,?)";
    private static final String DELETE_AUTHOR_BOOK_QUERY = """
            DELETE
            from author_book
            where book_id in (
                select book_id
                from author_book
                         join author on author.id = author_book.author_id
                         join book on book.id = author_book.book_id
                where book_id = ?
            )""";

    public void setAuthorToBook(int authorId, int bookId, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_AUTHOR_BOOK_QUERY)) {
            statement.setInt(1, authorId);
            statement.setInt(2, bookId);
            statement.execute();
        }
    }

    public void removeAuthorsFromBook(int bookId, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_AUTHOR_BOOK_QUERY)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }
}
