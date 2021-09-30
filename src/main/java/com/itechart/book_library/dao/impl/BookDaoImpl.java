package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.Dao;
import com.itechart.book_library.domain.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements Dao<Book> {

    private static final String CREATE = "INSERT INTO book (id, title, publisher, publish_date, page_count, isbn, description, cover) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ALL = "SELECT * FROM book";
    private static final String GET_BY_ID = "SELECT * FROM book WHERE id = ?";
    private static final String UPDATE = "UPDATE book SET title = ?, SET publisher = ?, SET publish_date = ?, SET page_count = ?, SET isbn = ?, SET description = ?, SET cover = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM book WHERE id = ?";
    private static final String GET_BY_TITLE = "SELECT * FROM book WHERE title = ?";
    private static final String GET_BY_AUTHOR = "SELECT * FROM book JOIN author_book ON book.id = author_book.author_id WHERE author_id = ?";
    private static final String GET_BY_GENRE = "SELECT * FROM book JOIN genre_book ON book.id = genre_book.genre_id WHERE genre_id = ?";
    private static final String GET_BY_DESCRIPTION = "SELECT * FROM book WHERE description LIKE '%?%'";

    private final Connection connection;

    public BookDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Book create(Book book) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setDate(3, book.getPublishDate());
            statement.setInt(4, book.getPageCount());
            statement.setString(5, book.getISBN());
            statement.setString(6, book.getDescription());
            statement.setString(7, book.getCover());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public List<Book> getAll() {
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL)) {
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> getById(int id) {
        return getListByKey(GET_BY_ID, id);
    }

    @Override
    public void update(Book book) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setDate(3, book.getPublishDate());
            statement.setInt(4, book.getPageCount());
            statement.setString(5, book.getISBN());
            statement.setString(6, book.getDescription());
            statement.setString(7, book.getCover());
            statement.setInt(8, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Book> getByTitle(String title) {
        return getListByKey(GET_BY_TITLE, title);
    }

    public List<Book> getByAuthor(int id) {
        return getListByKey(GET_BY_AUTHOR, id);
    }

    public List<Book> getByGenre(int id) {
        return getListByKey(GET_BY_GENRE, id);
    }

    public List<Book> getByDescription(String description) {
        return getListByKey(GET_BY_DESCRIPTION, description);
    }

    private List<Book> getListByKey(String query, int id) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Book> getListByKey(String query, String text) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Book> getListFromResultSet(ResultSet resultSet) {
        List<Book> books = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setPublisher(resultSet.getString(3));
                book.setPublishDate(resultSet.getDate(4));
                book.setPageCount(resultSet.getInt(5));
                book.setISBN(resultSet.getString(6));
                book.setDescription(resultSet.getString(7));
                book.setCover(resultSet.getString(8));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }


}
