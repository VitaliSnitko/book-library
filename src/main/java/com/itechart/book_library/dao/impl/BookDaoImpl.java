package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.GenreEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BookDaoImpl extends BaseDao implements BookDao {

    private static final Logger log = Logger.getLogger(BookDaoImpl.class);

    private static final String INSERT_BOOK_QUERY = "INSERT INTO book (id, title, publisher, publish_date, page_count, isbn, description, cover) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM book";
    private static final String SELECT_LIMIT_OFFSET_QUERY = """
            select book.*, author.*, genre.*
            from book
                     left join author_book on author_book.book_id = book.id
                     left join author on author_book.author_id = author.id
                     left join genre_book on genre_book.book_id = book.id
                     left join genre on genre_book.genre_id = genre.id
            where book.id in
            (select book.id from book order by book.id desc limit ? offset ?)""";
    private static final String SELECT_BY_ID_QUERY = """
            select book.*, author.*, genre.*
            from book
                     left join author_book on author_book.book_id = book.id
                     left join author on author_book.author_id = author.id
                     left join genre_book on genre_book.book_id = book.id
                     left join genre on genre_book.genre_id = genre.id
            where book.id = ?""";
    private static final String UPDATE_QUERY = "UPDATE book SET title = ?, publisher = ?, publish_date = ?, page_count = ?, isbn = ?, description = ?, cover = COALESCE(?, cover) WHERE id = ?";
    private static final String TEMPLATE_DELETE_QUERY = "DELETE FROM book WHERE id IN(?";
    private static String DELETE_QUERY = TEMPLATE_DELETE_QUERY;
    private static final String SELECT_BOOK_COUNT = "SELECT COUNT(*) FROM book";
    private static final String SELECT_BY_TITLE_QUERY = "SELECT * FROM book WHERE title = ?";
    private static final String SELECT_BY_AUTHOR_QUERY = "SELECT * FROM book JOIN author_book ON book.id = author_book.author_id WHERE author_id = ?";
    private static final String SELECT_BY_GENRE_QUERY = "SELECT * FROM book JOIN genre_book ON book.id = genre_book.genre_id WHERE genre_id = ?";
    private static final String SELECT_BY_DESCRIPTION_QUERY = "SELECT * FROM book WHERE description LIKE '%?%'";

    @Override
    public BookEntity create(BookEntity book) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_QUERY)) {
            int i = 1;
            statement.setString(i++, book.getTitle());
            statement.setString(i++, book.getPublisher());
            statement.setDate(i++, book.getPublishDate());
            statement.setInt(i++, book.getPageCount());
            statement.setString(i++, book.getISBN());
            statement.setString(i++, book.getDescription());
            statement.setBinaryStream(i, book.getCover());
            statement.execute();
            book.setId(getIdAfterInserting(statement));
        } catch (SQLException e) {
            log.error("Cannot create book ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return book;
    }

    @Override
    public List<BookEntity> getLimitOffset(int limit, int offset) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_LIMIT_OFFSET_QUERY)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            return getBookListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get books ", e);
            return null;
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    @Override
    public Optional<BookEntity> getById(int id) {
        List<BookEntity> bookList = getListByKey(SELECT_BY_ID_QUERY, id);
        return bookList.isEmpty() ? Optional.empty() : Optional.of(bookList.get(0));
    }

    @Override
    public void update(BookEntity book) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 1;
            statement.setString(i++, book.getTitle());
            statement.setString(i++, book.getPublisher());
            statement.setDate(i++, book.getPublishDate());
            statement.setInt(i++, book.getPageCount());
            statement.setString(i++, book.getISBN());
            statement.setString(i++, book.getDescription());
            statement.setBinaryStream(i++, book.getCover());
            statement.setInt(i, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot update book ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    @Override
    public void delete(Integer[] ids) {
        for (int i = 0; i < ids.length - 1; i++) {
            DELETE_QUERY += ",?";
        }
        DELETE_QUERY += ")";
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            for (int i = 0; i < ids.length; i++) {
                statement.setInt(i + 1, ids[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot delete book ", e);
        } finally {
            connectionPool.returnToPool(connection);
            DELETE_QUERY = TEMPLATE_DELETE_QUERY;
        }
    }

    public int getCount() {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BOOK_COUNT)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            log.error("Cannot get count of books ", e);
            return 0;
        } finally {
            connectionPool.returnToPool(connection);
            DELETE_QUERY = TEMPLATE_DELETE_QUERY;
        }
    }

//    @Override
//    public Optional<BookEntity> getByTitle(String title) throws SQLException {
//        List<BookEntity> rsList = getListByKey(SELECT_BY_TITLE_QUERY, title);
//        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
//    }
//
//    @Override
//    public List<BookEntity> getByAuthor(int id) throws SQLException {
//        return getListByKey(SELECT_BY_AUTHOR_QUERY, id);
//    }
//
//    @Override
//    public List<BookEntity> getByGenre(int id) throws SQLException {
//        return getListByKey(SELECT_BY_GENRE_QUERY, id);
//    }
//
//    @Override
//    public List<BookEntity> getByDescription(String description) throws SQLException {
//        return getListByKey(SELECT_BY_DESCRIPTION_QUERY, description);
//    }

    private List<BookEntity> getListByKey(String query, int id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getBookListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get list by " + id + " key ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<BookEntity> getListByKey(String query, String text) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getBookListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get list by " + text + " key ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<BookEntity> getBookListFromResultSet(ResultSet resultSet) throws SQLException {
        int prevBookId = 0;
        if (resultSet.next()) {
            prevBookId = resultSet.getInt(1);
        }

        BookEntity book = getBookWithParams(resultSet);
        List<BookEntity> books = new ArrayList<>();
        Set<AuthorEntity> authorSet = new HashSet<>();
        Set<GenreEntity> genreSet = new HashSet<>();

        do {
            if (prevBookId != resultSet.getInt(1)) {
                book.setAuthorEntities(new ArrayList<>(authorSet));
                book.setGenreEntities(new ArrayList<>(genreSet));
                authorSet.clear(); genreSet.clear();
                books.add(book);
            }
            AuthorEntity author = new AuthorEntity();
            author.setId(resultSet.getInt(9));
            author.setName(resultSet.getString(10));

            GenreEntity genre = new GenreEntity();
            genre.setId(resultSet.getInt(11));
            genre.setName(resultSet.getString(12));

            genreSet.add(genre);
            authorSet.add(author);
            book = getBookWithParams(resultSet);

            prevBookId = resultSet.getInt(1);
        } while (resultSet.next());
        book.setAuthorEntities(new ArrayList<>(authorSet));
        book.setGenreEntities(new ArrayList<>(genreSet));
        books.add(book);
        return books;
    }

    private BookEntity getBookWithParams(ResultSet resultSet) throws SQLException {
        BookEntity book = new BookEntity();
        int i = 1;
        book.setId(resultSet.getInt(i++));
        book.setTitle(resultSet.getString(i++));
        book.setPublisher(resultSet.getString(i++));
        book.setPublishDate(resultSet.getDate(i++));
        book.setPageCount(resultSet.getInt(i++));
        book.setISBN(resultSet.getString(i++));
        book.setDescription(resultSet.getString(i++));
        book.setCover(resultSet.getBinaryStream(i));
        return book;
    }
}
