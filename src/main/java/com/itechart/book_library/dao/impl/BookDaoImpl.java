package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.BookDao;
import com.itechart.book_library.dao.criteria.BookSpecification;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.GenreEntity;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Log4j
public class BookDaoImpl extends BaseDao implements BookDao {

    private static final String INSERT_BOOK_QUERY = """
            INSERT INTO book (id, title, publisher, publish_date, page_count, isbn, description, cover, available, total_amount)
            VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id""";
    private static final String SELECT_LIMIT_OFFSET_WITH_PARAMETERS_QUERY = """
            with parameters(title_p, authors_p, genres_p, description_p) as (
                values (?, ?, ?, ?)
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
                              order by book.id desc
                              limit ? offset ?);""";
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
                available    = ? - total_amount + available,
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
              and description ~* ?;""";
    private static final String UPDATE_TAKE_BOOK_QUERY = "UPDATE book SET available = available-1 WHERE id = ?";
    private static final StringBuilder TEMPLATE_DELETE_QUERY = new StringBuilder("DELETE FROM book WHERE id IN(?");
    private static StringBuilder DELETE_QUERY = TEMPLATE_DELETE_QUERY;

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
            statement.setInt(i++, limit);
            statement.setInt(i++, offset);
            return getBookListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get books ", e);
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
    public void update(BookEntity book, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 1;
            statement.setString(i++, book.getTitle());
            statement.setString(i++, book.getPublisher());
            statement.setDate(i++, book.getPublishDate());
            statement.setInt(i++, book.getPageCount());
            statement.setString(i++, book.getISBN());
            statement.setString(i++, book.getDescription());
            statement.setBinaryStream(i++, book.getCover());
            statement.setInt(i++, book.getTotalBookAmount());
            statement.setInt(i++, book.getTotalBookAmount());
            statement.setInt(i++, book.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer[] ids) {
        DELETE_QUERY.append(",?".repeat(Math.max(0, ids.length - 1)));
        DELETE_QUERY.append(")");
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(String.valueOf(DELETE_QUERY))) {
            for (int i = 0; i < ids.length; i++) {
                statement.setInt(i + 1, ids[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot delete books ", e);
        } finally {
            connectionPool.returnToPool(connection);
            DELETE_QUERY = TEMPLATE_DELETE_QUERY;
        }
    }

    public int getCountBySpecification(BookSpecification specification) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BOOK_COUNT_WITH_PARAMETERS)) {
            statement.setString(1, specification.getTitle());
            statement.setString(2, specification.getAuthors());
            statement.setString(3, specification.getGenres());
            statement.setString(4, specification.getDescription());
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

    public void takeBook(int id, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TAKE_BOOK_QUERY)) {
            statement.setInt(1, id);
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
            if (prevBookId != resultSet.getInt(1) && resultSetIndex != 0) {
                book.setAuthorEntities(new ArrayList<>(authorSet));
                book.setGenreEntities(new ArrayList<>(genreSet));
                authorSet.clear();
                genreSet.clear();
                books.add(book);
            }
            book = getBookByResultSet(resultSet);
            authorSet.add(getAuthorByResultSet(resultSet));
            genreSet.add(getGenreByResultSet(resultSet));

            prevBookId = resultSet.getInt(1);
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
                .id(resultSet.getInt(11))
                .name(resultSet.getString(12))
                .build();
    }

    private GenreEntity getGenreByResultSet(ResultSet resultSet) throws SQLException {
        return GenreEntity.builder()
                .id(resultSet.getInt(13))
                .name(resultSet.getString(14))
                .build();
    }
}
