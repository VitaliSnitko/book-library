package com.itechart.library.dao.impl;

import com.itechart.library.dao.AuthorDao;
import com.itechart.library.dao.BaseDao;
import com.itechart.library.model.entity.AuthorEntity;
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
public class AuthorDaoImpl extends BaseDao implements AuthorDao {

    private static final String INSERT_QUERY = "INSERT INTO author (id, name) VALUES (DEFAULT, ?) RETURNING id";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM author WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM author WHERE name = ?";
    public static final String ID_LABEL = "id";
    public static final String NAME_LABEL = "name";

    @Override
    public AuthorEntity create(AuthorEntity author, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            int i = 1;
            statement.setString(i++, author.getName());
            statement.execute();
            author.setId(getIdAfterInserting(statement));
        }
        return author;
    }

    @Override
    public Optional<AuthorEntity> getById(int id) {
        List<AuthorEntity> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public AuthorEntity update(AuthorEntity entity, Connection connection) {
        throw new NotImplementedException("This method is not implemented for AuthorDao");
    }

    @Override
    public void delete(Integer[] ids) {
        throw new NotImplementedException("This method is not implemented for AuthorDao");
    }

    @Override
    public void delete(Integer[] ids, Connection connection) throws SQLException {
        throw new NotImplementedException("This method is not implemented for AuthorDao");
    }

    @Override
    public Optional<AuthorEntity> getByName(String name) {
        List<AuthorEntity> rsList = getListByKey(SELECT_BY_NAME_QUERY, name);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    private List<AuthorEntity> getListByKey(String query, int id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get author list by " + id + " key ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<AuthorEntity> getListByKey(String query, String text) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get author list by " + text + " key ", e);
            return new ArrayList<>();
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<AuthorEntity> getListFromResultSet(ResultSet resultSet) {
        List<AuthorEntity> authorEntities = new ArrayList<>();
        try {
            while (resultSet.next()) {
                authorEntities.add(AuthorEntity.builder()
                        .id(resultSet.getInt(ID_LABEL))
                        .name(resultSet.getString(NAME_LABEL))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Result set error ", e);
        }
        return authorEntities;
    }
}
