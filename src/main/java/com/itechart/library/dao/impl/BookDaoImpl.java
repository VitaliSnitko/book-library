package com.itechart.library.dao.impl;

import com.itechart.library.dao.BaseDao;
import com.itechart.library.dao.BookDao;
import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.model.entity.AuthorEntity;
import com.itechart.library.model.entity.BookEntity;
import com.itechart.library.model.entity.GenreEntity;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("UnusedAssignment")
@Log4j
public class BookDaoImpl extends BaseDao implements BookDao {

    private static final String INSERT_BOOK_QUERY = """
            INSERT INTO book (id, title, publisher, publish_date, page_count, isbn, description, cover, available, total_amount)
            VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id""";
    private static final String SELECT_LIMIT_OFFSET_WITH_PARAMETERS_QUERY = """
            with parameters(title_p, authors_p, genres_p, description_p, only_available_p) as (
                values (?, ?, ?, ?, ?)
            )
            select distinct book.*, author.*, genre.*
            from parameters, book
                     left join author_book on author_book.book_id = book.id
                     left join author on author_book.author_id = author.id
                     left join genre_book on genre_book.book_id = book.id
                     left join genre on genre_book.genre_id = genre.id
            WHERE title ~* title_p
              and author.name ~* authors_p
              and genre.name ~* genres_p
              and description ~* description_p
              and case when only_available_p then available != 0 else true end
              and book.id in (select distinct book.id
                              from parameters, book
                                       left join author_book on author_book.book_id = book.id
                                       left join author on author_book.author_id = author.id
                                       left join genre_book on genre_book.book_id = book.id
                                       left join genre on genre_book.genre_id = genre.id
                              WHERE title ~* title_p
                                and author.name ~* authors_p
                                and genre.name ~* genres_p
                                and description ~* description_p
                                and case when only_available_p then available != 0 else true end
                              order by book.id desc
                              limit ? offset ?)
            order by book.id desc;""";
    private static final String SELECT_BY_ID_QUERY = """
            select book.*, author.*, genre.*
            from book
                     left join author_book on author_book.book_id = book.id
                     left join author on author_book.author_id = author.id
                     left join genre_book on genre_book.book_id = book.id
                     left join genre on genre_book.genre_id = genre.id
            where book.id = ?""";
    private static final String UPDATE_QUERY = """
            UPDATE book
            SET title        = ?,
                publisher    = ?,
                publish_date = ?,
                page_count   = ?,
                isbn         = ?,
                description  = ?,
                cover        = COALESCE(?, cover),
                available    = ?,
                total_amount = ?
            WHERE id = ?""";
    private static final String SELECT_BOOK_COUNT_WITH_PARAMETERS = """
            select count(DISTINCT book.id) from
            book
                     left join author_book on author_book.book_id = book.id
                     left join author on author_book.author_id = author.id
                     left join genre_book on genre_book.book_id = book.id
                     left join genre on genre_book.genre_id = genre.id
            WHERE title ~* ?
              and author.name ~* ?
              and genre.name ~* ?
              and description ~* ?
              and case when ? then available != 0 else true end;""";
    private static final String SELECT_AVAILABLE_QUERY = "SELECT book.available as count FROM book WHERE id = ?";
    private static final String SELECT_TOTAL_QUERY = "SELECT book.total_amount as count FROM book WHERE id = ?";
    private static final String UPDATE_AVAILABLE_QUERY = "UPDATE book SET available = ? WHERE id = ?";
    private static final String UPDATE_TOTAL_AMOUNT_QUERY = "UPDATE book SET total_amount = ? WHERE id = ?";
    private static final StringBuilder TEMPLATE_DELETE_QUERY = new StringBuilder("DELETE FROM book WHERE id IN(?");
    public static final int BOOK_ID_COLUMN_INDEX = 1;
    public static final int AUTHOR_ID_COLUMN_INDEX = 11;
    public static final int AUTHOR_NAME_COLUMN_INDEX = 12;
    public static final int GENRE_ID_COLUMN_INDEX = 13;
    public static final int GENRE_NAME_COLUMN_INDEX = 14;
    public static final String COUNT_LABEL = "count";

    @Override
    public BookEntity create(BookEntity book, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_QUERY)) {
            int i = 1;
            statement.setString(i++, book.getTitle());
            statement.setString(i++, book.getPublisher());
            statement.setDate(i++, book.getPublishDate());
            statement.setInt(i++, book.getPageCount());
            statement.setString(i++, book.getISBN());
            statement.setString(i++, book.getDescription());
            statement.setBinaryStream(i++, book.getCover());
            statement.setInt(i++, book.getAvailableBookAmount());
            statement.setInt(i++, book.getTotalBookAmount());
            statement.execute();
            book.setId(getIdAfterInserting(statement));
        }
        return book;
    }

    @Override
    public List<BookEntity> getLimitOffsetBySpecification(BookSpecification specification, int limit, int offset) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_LIMIT_OFFSET_WITH_PARAMETERS_QUERY)) {
            int i = 1;
            statement.setString(i++, specification.getTitle());
            statement.setString(i++, specification.getAuthors());
            statement.setString(i++, specification.getGenres());
            statement.setString(i++, specification.getDescription());
            statement.setBoolean(i++, specification.isOnlyAvailable());
            statement.setInt(i++, limit);
            statement.setInt(i++, offset);
            return getBookListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error(e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<BookEntity> getById(int id) {
        List<BookEntity> bookList = getListByKey(SELECT_BY_ID_QUERY, id);
        return bookList.isEmpty() ? Optional.empty() : Optional.of(bookList.get(0));
    }

    @Override
    public BookEntity update(BookEntity book, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 1;
            statement.setString(i++, book.getTitle());
            statement.setString(i++, book.getPublisher());
            statement.setDate(i++, book.getPublishDate());
            statement.setInt(i++, book.getPageCount());
            statement.setString(i++, book.getISBN());
            statement.setString(i++, book.getDescription());
            statement.setBinaryStream(i++, book.getCover());
            statement.setInt(i++, book.getAvailableBookAmount());
            statement.setInt(i++, book.getTotalBookAmount());
            statement.setInt(i++, book.getId());
            statement.executeUpdate();
        }
        return book;
    }

    @Override
    public void delete(Integer[] ids) {
        StringBuilder deleteQuery = TEMPLATE_DELETE_QUERY;
        deleteQuery.append(",?".repeat(Math.max(0, ids.length - 1)));
        deleteQuery.append(")");
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(String.valueOf(deleteQuery))) {
            for (int i = 0; i < ids.length; i++) {
                statement.setInt(i + 1, ids[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    @Override
    public void delete(Integer[] ids, Connection connection) throws SQLException {
    }

    @Override
    public int getCount(BookSpecification specification) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BOOK_COUNT_WITH_PARAMETERS)) {
            int i = 1;
            statement.setString(i++, specification.getTitle());
            statement.setString(i++, specification.getAuthors());
            statement.setString(i++, specification.getGenres());
            statement.setString(i++, specification.getDescription());
            statement.setBoolean(i++, specification.isOnlyAvailable());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException e) {
            log.error(e);
            return -1;
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    @Override
    public int getAvailableBookCount(int id) {
        return getCount(id, SELECT_AVAILABLE_QUERY);
    }

    @Override
    public int getTotalBookAmount(int id) {
        return getCount(id, SELECT_TOTAL_QUERY);
    }

    private int getCount(int id, String query) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(COUNT_LABEL);
        } catch (SQLException e) {
            log.error(e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return -1;
    }

    public void updateAvailableAmount(int amount, int id, Connection connection) throws SQLException {
        updateAmount(amount, id, connection, UPDATE_AVAILABLE_QUERY);
    }

    public void updateTotalAmount(int amount, int id, Connection connection) throws SQLException {
        updateAmount(amount, id, connection, UPDATE_TOTAL_AMOUNT_QUERY);
    }

    private void updateAmount(int amount, int id, Connection connection, String updateTotalAmountQuery) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(updateTotalAmountQuery)) {
            int i = 1;
            statement.setInt(i++, amount);
            statement.setInt(i++, id);
            statement.executeUpdate();
        }
    }

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

    private List<BookEntity> getBookListFromResultSet(ResultSet resultSet) throws SQLException {
        List<BookEntity> books = new ArrayList<>();
        BookEntity book = BookEntity.builder().build();
        Set<AuthorEntity> authorSet = new HashSet<>();
        Set<GenreEntity> genreSet = new HashSet<>();

        int prevBookId = -1;
        int resultSetIndex = 0;
        while (resultSet.next()) {
            if (prevBookId != resultSet.getInt(BOOK_ID_COLUMN_INDEX) && resultSetIndex != 0) {
                book.setAuthorEntities(new ArrayList<>(authorSet));
                book.setGenreEntities(new ArrayList<>(genreSet));
                authorSet.clear();
                genreSet.clear();
                books.add(book);
            }
            book = getBookByResultSet(resultSet);
            authorSet.add(getAuthorByResultSet(resultSet));
            genreSet.add(getGenreByResultSet(resultSet));

            prevBookId = resultSet.getInt(BOOK_ID_COLUMN_INDEX);
            resultSetIndex++;
        }
        if (resultSetIndex == 0) {
            return books;
        }
        book.setAuthorEntities(new ArrayList<>(authorSet));
        book.setGenreEntities(new ArrayList<>(genreSet));
        books.add(book);
        return books;
    }

    private BookEntity getBookByResultSet(ResultSet resultSet) throws SQLException {
        int i = 1;
        return BookEntity.builder()
                .id(resultSet.getInt(i++))
                .title(resultSet.getString(i++))
                .publisher(resultSet.getString(i++))
                .publishDate(resultSet.getDate(i++))
                .pageCount(resultSet.getInt(i++))
                .ISBN(resultSet.getString(i++))
                .description(resultSet.getString(i++))
                .cover(resultSet.getBinaryStream(i++))
                .availableBookAmount(resultSet.getInt(i++))
                .totalBookAmount(resultSet.getInt(i++))
                .build();

    }

    private AuthorEntity getAuthorByResultSet(ResultSet resultSet) throws SQLException {
        return AuthorEntity.builder()
                .id(resultSet.getInt(AUTHOR_ID_COLUMN_INDEX))
                .name(resultSet.getString(AUTHOR_NAME_COLUMN_INDEX))
                .build();
    }

    private GenreEntity getGenreByResultSet(ResultSet resultSet) throws SQLException {
        return GenreEntity.builder()
                .id(resultSet.getInt(GENRE_ID_COLUMN_INDEX))
                .name(resultSet.getString(GENRE_NAME_COLUMN_INDEX))
                .build();
    }
}
