package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorBookDaoImpl extends BaseDao {

    private static final Logger log = Logger.getLogger(AuthorBookDaoImpl.class);
    private static final String INSERT_AUTHOR_BOOK_QUERY = "INSERT INTO author_book (author_id, book_id) VALUES (?,?)";

    public void setAuthorToBook(int authorId, int bookId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_AUTHOR_BOOK_QUERY)) {
            statement.setInt(1, authorId);
            statement.setInt(2, bookId);
            statement.execute();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error(ex);
            }
            log.error("Cannot insert into author_book", e);
        }
    }
}
