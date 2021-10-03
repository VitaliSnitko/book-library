package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl extends BaseDao implements BookDao {

    private static final String INSERT_BOOK_QUERY = "INSERT INTO book (id, title, publisher, publish_date, page_count, isbn, description, cover) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM book";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM book WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE book SET title = ?, SET publisher = ?, SET publish_date = ?, SET page_count = ?, SET isbn = ?, SET description = ?, SET cover = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM book WHERE id = ?";
    private static final String SELECT_BY_TITLE_QUERY = "SELECT * FROM book WHERE title = ?";
    private static final String SELECT_BY_AUTHOR_QUERY = "SELECT * FROM book JOIN author_book ON book.id = author_book.author_id WHERE author_id = ?";
    private static final String SELECT_BY_GENRE_QUERY = "SELECT * FROM book JOIN genre_book ON book.id = genre_book.genre_id WHERE genre_id = ?";
    private static final String SELECT_BY_DESCRIPTION_QUERY = "SELECT * FROM book WHERE description LIKE '%?%'";
    private static final String INSERT_AUTHOR_BOOK_QUERY = "INSERT INTO author_book (author_id, book_id) VALUES (?,?)";
    private static final String INSERT_GENRE_BOOK_QUERY = "INSERT INTO genre_book (genre_id, book_id) VALUES (?,?)";

    @Override
    public Book create(Book book) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_QUERY)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setDate(3, book.getPublishDate());
            statement.setInt(4, book.getPageCount());
            statement.setString(5, book.getISBN());
            statement.setString(6, book.getDescription());
            statement.setBinaryStream(7, book.getCover());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public List<Book> getAll() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book getById(int id) {
        return getListByKey(SELECT_BY_ID_QUERY, id).get(0);
    }

    @Override
    public void update(Book book) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book getByTitle(String title) {
        return getListByKey(SELECT_BY_TITLE_QUERY, title).get(0);
    }

    @Override
    public List<Book> getByAuthor(int id) {
        return getListByKey(SELECT_BY_AUTHOR_QUERY, id);
    }

    @Override
    public List<Book> getByGenre(int id) {
        return getListByKey(SELECT_BY_GENRE_QUERY, id);
    }

    @Override
    public List<Book> getByDescription(String description) {
        return getListByKey(SELECT_BY_DESCRIPTION_QUERY, description);
    }

    @Override
    public void setAuthorToBook(int authorId, int bookId) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_AUTHOR_BOOK_QUERY)) {
            statement.setInt(1, authorId);
            statement.setInt(2, bookId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setGenreToBook(int genreId, int bookId) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_GENRE_BOOK_QUERY)) {
            statement.setInt(1, genreId);
            statement.setInt(2, bookId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Book> getListByKey(String query, int id){
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
                book.setCover(resultSet.getBinaryStream(8));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }


}
