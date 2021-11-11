package com.itechart.library.dao.impl;

import com.itechart.library.dao.BaseDao;
import com.itechart.library.dao.GenreDao;
import com.itechart.library.model.entity.GenreEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedAssignment")
public class GenreDaoImpl extends BaseDao implements GenreDao {

    private static final Logger log = Logger.getLogger(GenreDaoImpl.class);

    private static final String INSERT_QUERY = "INSERT INTO genre (id, name) VALUES (DEFAULT, ?) RETURNING id";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = ?";
    public static final String ID_LABEL = "id";
    public static final String NAME_LABEL = "name";
    public static final String NOT_IMPLEMENTED_MESSAGE = "This method is not implemented for GenreDao";

    @Override
    public GenreEntity create(GenreEntity genre, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            int i = 1;
            statement.setString(i++, genre.getName());
            statement.execute();
            genre.setId(getIdAfterInserting(statement));
        }
        return genre;
    }

    @Override
    public Optional<GenreEntity> getById(int id) {
        List<GenreEntity> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public GenreEntity update(GenreEntity entity, Connection connection) {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public void delete(Integer[] ids) {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public void delete(Integer[] ids, Connection connection) throws SQLException {
        throw new NotImplementedException(NOT_IMPLEMENTED_MESSAGE);
    }

    @Override
    public Optional<GenreEntity> getByName(String name) {
        List<GenreEntity> rsList = getListByKey(SELECT_BY_NAME_QUERY, name);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    private List<GenreEntity> getListByKey(String query, int id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get list by " + id + " key ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<GenreEntity> getListByKey(String query, String text) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get list by " + text + " key ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<GenreEntity> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<GenreEntity> genreEntities = new ArrayList<>();
        while (resultSet.next()) {
            genreEntities.add(GenreEntity.builder()
                    .id(resultSet.getInt(ID_LABEL))
                    .name(resultSet.getString(NAME_LABEL))
                    .build());
        }
        return genreEntities;
    }
}
