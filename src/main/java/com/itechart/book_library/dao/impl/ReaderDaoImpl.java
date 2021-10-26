package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.ReaderDao;
import com.itechart.book_library.model.entity.ReaderEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDaoImpl extends BaseDao implements ReaderDao {

    private static final Logger log = Logger.getLogger(ReaderDaoImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO reader (id, email, first_name) VALUES (DEFAULT, ?, ?) RETURNING id";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM reader";
    private static final String SELECT_BY_EMAIL_QUERY = "SELECT * FROM reader WHERE email = ?";
    private static final String SELECT_BY_EMAIL_AND_BOOK_QUERY = """
            SELECT reader.* FROM reader
            JOIN record ON reader.id = record.reader_id
            WHERE email = ? AND record.book_id = ?;""";

    @Override
    public ReaderEntity create(ReaderEntity reader, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, reader.getEmail());
            statement.setString(2, reader.getName());
            statement.execute();
            reader.setId(getIdAfterInserting(statement));
        }
        return reader;
    }

    @Override
    public Optional<ReaderEntity> getById(int id) {
        return Optional.empty();
    }

    public List<ReaderEntity> getAll() {
        Connection connection = connectionPool.getConnection();
        List<ReaderEntity> readerEntity = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                readerEntity.add(ReaderEntity.builder()
                        .id(resultSet.getInt(1))
                        .email(resultSet.getString(2))
                        .name(resultSet.getString(3)).build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return readerEntity;
    }

    @Override
    public void update(ReaderEntity entity, Connection connection) {
    }

    @Override
    public void delete(Integer[] ids) {
    }

    public Optional<ReaderEntity> getByEmail(String email, Connection connection) {
        Optional<ReaderEntity> readerEntity = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_QUERY)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                readerEntity = Optional.of(ReaderEntity.builder()
                        .id(resultSet.getInt(1))
                        .email(resultSet.getString(2))
                        .name(resultSet.getString(3)).build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        }
        return readerEntity;
    }

    public Optional<ReaderEntity> getByEmailInCurrentBook(String email, int bookId, Connection connection) {
        Optional<ReaderEntity> readerEntity = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_AND_BOOK_QUERY)) {
            statement.setString(1, email);
            statement.setInt(2, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                readerEntity = Optional.of(ReaderEntity.builder()
                        .id(resultSet.getInt(1))
                        .email(resultSet.getString(2))
                        .name(resultSet.getString(3)).build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        }
        return readerEntity;
    }
}
