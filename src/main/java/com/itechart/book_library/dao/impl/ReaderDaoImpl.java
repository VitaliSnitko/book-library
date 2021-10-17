package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.ReaderDao;
import com.itechart.book_library.model.entity.ReaderEntity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReaderDaoImpl extends BaseDao implements ReaderDao {

    private static final Logger log = Logger.getLogger(ReaderDaoImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO reader (id, email, first_name) VALUES (DEFAULT, ?, ?) RETURNING id";
    private static final String SELECT_BY_EMAIL_QUERY = "SELECT * FROM reader WHERE email = ?";
    private static final String UPDATE_QUERY = "UPDATE reader SET first_name = ? WHERE id = ?";


    @Override
    public ReaderEntity create(ReaderEntity reader) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, reader.getEmail());
            statement.setString(2, reader.getName());
            statement.execute();
            reader.setId(getIdAfterInserting(statement));
        } catch (SQLException e) {
            log.error("Cannot create record ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return reader;
    }

    @Override
    public Optional<ReaderEntity> getById(int id) {
        return Optional.empty();
    }

    @Override
    public void update(ReaderEntity reader) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, reader.getName());
            statement.setInt(2, reader.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Cannot create record ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    @Override
    public void delete(Integer[] ids) {

    }

    public Optional<ReaderEntity> getByEmail(String email) {
        Connection connection = connectionPool.getConnection();
        Optional<ReaderEntity> readerEntity = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_QUERY)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                readerEntity = Optional.of(new ReaderEntity(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return readerEntity;
    }
}
