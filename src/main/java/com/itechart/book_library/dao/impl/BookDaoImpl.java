package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.model.entity.BookEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl extends BaseDao implements BookDao {

    private static final String INSERT_BOOK_QUERY = "INSERT INTO book (id, title, publisher, publish_date, page_count, isbn, description, cover) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM book";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM book WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE book SET title = ?, publisher = ?, publish_date = ?, page_count = ?, isbn = ?, description = ?, cover = COALESCE(?, cover) WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM book WHERE id = ?";
    private static final String SELECT_BY_TITLE_QUERY = "SELECT * FROM book WHERE title = ?";
    private static final String SELECT_BY_AUTHOR_QUERY = "SELECT * FROM book JOIN author_book ON book.id = author_book.author_id WHERE author_id = ?";
    private static final String SELECT_BY_GENRE_QUERY = "SELECT * FROM book JOIN genre_book ON book.id = genre_book.genre_id WHERE genre_id = ?";
    private static final String SELECT_BY_DESCRIPTION_QUERY = "SELECT * FROM book WHERE description LIKE '%?%'";

    @Override
    public BookEntity create(BookEntity book) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_QUERY)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setDate(3, book.getPublishDate());
            statement.setInt(4, book.getPageCount());
            statement.setString(5, book.getISBN());
            statement.setString(6, book.getDescription());
            statement.setBinaryStream(7, book.getCover());
            statement.execute();
            book.setId(getIdAfterInserting(statement));
        }
        return book;
    }

    @Override
    public List<BookEntity> getAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            return getListFromResultSet(statement.executeQuery());
        }
    }

    @Override
    public Optional<BookEntity> getById(int id) throws SQLException {
        List<BookEntity> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public void update(BookEntity book) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setDate(3, book.getPublishDate());
            statement.setInt(4, book.getPageCount());
            statement.setString(5, book.getISBN());
            statement.setString(6, book.getDescription());
            statement.setBinaryStream(7, book.getCover());
            statement.setInt(8, book.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<BookEntity> getByTitle(String title) throws SQLException {
        List<BookEntity> rsList = getListByKey(SELECT_BY_TITLE_QUERY, title);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public List<BookEntity> getByAuthor(int id) throws SQLException {
        return getListByKey(SELECT_BY_AUTHOR_QUERY, id);
    }

    @Override
    public List<BookEntity> getByGenre(int id) throws SQLException {
        return getListByKey(SELECT_BY_GENRE_QUERY, id);
    }

    @Override
    public List<BookEntity> getByDescription(String description) throws SQLException {
        return getListByKey(SELECT_BY_DESCRIPTION_QUERY, description);
    }

    private List<BookEntity> getListByKey(String query, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        }
    }

    private List<BookEntity> getListByKey(String query, String text) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
        }
    }

    private List<BookEntity> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<BookEntity> books = new ArrayList<>();
        while (resultSet.next()) {
            BookEntity book = new BookEntity();
            book.setId(resultSet.getInt(1));
            book.setTitle(resultSet.getString(2));
            book.setPublisher(resultSet.getString(3));
            book.setPublishDate(resultSet.getDate(4));
            book.setPageCount(resultSet.getInt(5));
            book.setISBN(resultSet.getString(6));
            book.setDescription(resultSet.getString(7));
            book.setCover(resultSet.getBinaryStream(8));
            books.add(book);
        }
        return books;
    }


}
