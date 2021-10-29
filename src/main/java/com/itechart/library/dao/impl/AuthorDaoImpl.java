package com.itechart.library.dao.impl;

import com.itechart.library.dao.AuthorDao;
import com.itechart.library.dao.BaseDao;
import com.itechart.library.model.entity.AuthorEntity;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
public class AuthorDaoImpl extends BaseDao implements AuthorDao {

    private static final String INSERT_QUERY = "INSERT INTO author (id, name) VALUES (DEFAULT, ?) RETURNING id";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM author WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM author WHERE name = ?";
    private static final StringBuilder TEMPLATE_DELETE_QUERY = new StringBuilder("DELETE FROM author WHERE name IN(?");
    private static StringBuilder DELETE_QUERY = TEMPLATE_DELETE_QUERY;

    @Override
    public AuthorEntity create(AuthorEntity author, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, author.getName());
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
    public void update(AuthorEntity entity, Connection connection) {
    }

    @Override
    public void delete(Integer[] ids) {
    }

    @Override
    public void delete(Integer[] ids, Connection connection) throws SQLException {
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
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .build());
            }
        } catch (SQLException e) {
            log.error("Result set error ", e);
        }
        return authorEntities;
    }
}
