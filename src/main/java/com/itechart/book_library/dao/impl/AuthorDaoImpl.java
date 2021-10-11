package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.AuthorDao;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.model.entity.AuthorEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl extends BaseDao implements AuthorDao {

    private static final String INSERT_QUERY = "INSERT INTO author (id, name) VALUES (DEFAULT, ?) RETURNING id";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM author";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM author WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM author WHERE name = ?";
    private static final String UPDATE_QUERY = "UPDATE author SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM author WHERE id = ?";
    private static final String SELECT_BY_BOOK_ID_QUERY = "SELECT * FROM author JOIN author_book ON author.id = author_book.author_id JOIN book ON author_book.book_id = book.id WHERE book_id = ?";

    @Override
    public AuthorEntity create(AuthorEntity authorEntity) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, authorEntity.getName());
            statement.execute();
            authorEntity.setId(getIdAfterInserting(statement));
        } finally {
            connectionPool.removeToPool(connection);

        }
        return authorEntity;
    }

    @Override
    public List<AuthorEntity> getAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            return getListFromResultSet(statement.executeQuery());
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    @Override
    public Optional<AuthorEntity> getById(int id) throws SQLException {
        List<AuthorEntity> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public void update(AuthorEntity authorEntity) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, authorEntity.getName());
            statement.executeUpdate();
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    @Override
    public Optional<AuthorEntity> getByName(String name) throws SQLException {
        List<AuthorEntity> rsList = getListByKey(SELECT_BY_NAME_QUERY, name);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public List<AuthorEntity> getByBookId(int id) throws SQLException {
        return getListByKey(SELECT_BY_BOOK_ID_QUERY, id);
    }

    private List<AuthorEntity> getListByKey(String query, int id) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    private List<AuthorEntity> getListByKey(String query, String text) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet( statement.executeQuery());
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    private List<AuthorEntity> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<AuthorEntity> authorEntities = new ArrayList<>();
        while (resultSet.next()) {
            AuthorEntity authorEntity = new AuthorEntity();
            authorEntity.setId(resultSet.getInt(1));
            authorEntity.setName(resultSet.getString(2));
            authorEntities.add(authorEntity);
        }
        return authorEntities;
    }
}
