package com.itechart.library.dao.impl;

import com.itechart.library.dao.BaseDao;
import com.itechart.library.dao.ReaderDao;
import com.itechart.library.model.entity.ReaderEntity;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedAssignment")
@Log4j
public class ReaderDaoImpl extends BaseDao implements ReaderDao {

    private static final String INSERT_QUERY = "INSERT INTO reader (id, email, first_name) VALUES (DEFAULT, ?, ?) RETURNING id";
    private static final String UPDATE_QUERY = "UPDATE reader SET first_name = ? where email = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM reader";
    private static final String SELECT_BY_EMAIL_QUERY = "SELECT * FROM reader WHERE email = ?";
    public static final String ID_LABEL = "id";
    public static final String EMAIL_LABEL = "email";
    public static final String FIRST_NAME_LABEL = "first_name";

    @Override
    public ReaderEntity create(ReaderEntity reader, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            int i = 1;
            statement.setString(i++, reader.getEmail());
            statement.setString(i++, reader.getName());
            statement.execute();
            reader.setId(getIdAfterInserting(statement));
        }
        return reader;
    }

    @Override
    public Optional<ReaderEntity> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<ReaderEntity> getAll() {
        Connection connection = connectionPool.getConnection();
        List<ReaderEntity> readerEntity = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                readerEntity.add(ReaderEntity.builder()
                        .id(resultSet.getInt(ID_LABEL))
                        .email(resultSet.getString(EMAIL_LABEL))
                        .name(resultSet.getString(FIRST_NAME_LABEL))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        } finally {
            connectionPool.returnToPool(connection);
        }
        return readerEntity;
    }

    @Override
    public ReaderEntity update(ReaderEntity reader, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 1;
            statement.setString(i++, reader.getName());
            statement.setString(i++, reader.getEmail());
            statement.executeUpdate();
        }
        return reader;
    }

    @Override
    public void delete(Integer[] ids) {
        throw new NotImplementedException("This method is not implemented for ReaderDao");
    }

    @Override
    public void delete(Integer[] ids, Connection connection) throws SQLException {
        throw new NotImplementedException("This method is not implemented for ReaderDao");
    }

    @Override
    public Optional<ReaderEntity> getByEmail(String email, Connection connection) {
        Optional<ReaderEntity> readerEntity = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMAIL_QUERY)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                readerEntity = Optional.of(ReaderEntity.builder()
                        .id(resultSet.getInt(ID_LABEL))
                        .email(resultSet.getString(EMAIL_LABEL))
                        .name(resultSet.getString(FIRST_NAME_LABEL))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Cannot get by email ", e);
        }
        return readerEntity;
    }
}
