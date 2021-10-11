package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.GenreDao;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.GenreEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDaoImpl extends BaseDao implements GenreDao {

    private static final String INSERT_QUERY = "INSERT INTO genre (id, name) VALUES (DEFAULT, ?) RETURNING id";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM genre";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = ?";
    private static final String UPDATE_QUERY = "UPDATE genre SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM genre WHERE id = ?";
    private static final String SELECT_BY_BOOK_ID_QUERY = "SELECT * FROM genre JOIN genre_book ON genre.id = genre_book.genre_id JOIN book ON genre_book.book_id = book.id WHERE book_id = ?";

    @Override
    public GenreEntity create(GenreEntity genreEntity) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, genreEntity.getName());
            statement.execute();
            genreEntity.setId(getIdAfterInserting(statement));
        } finally {
            connectionPool.removeToPool(connection);
        }
        return genreEntity;
    }

    @Override
    public List<GenreEntity> getAll() throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            return getListFromResultSet(statement.executeQuery());
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    @Override
    public Optional<GenreEntity> getById(int id) throws SQLException {
        List<GenreEntity> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public void update(GenreEntity genreEntity) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, genreEntity.getName());
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
    public Optional<GenreEntity> getByName(String name) throws SQLException {
        List<GenreEntity> rsList = getListByKey(SELECT_BY_NAME_QUERY, name);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public List<GenreEntity> getByBookId(int id) throws SQLException {
        return getListByKey(SELECT_BY_BOOK_ID_QUERY, id);
    }


    private List<GenreEntity> getListByKey(String query, int id) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    private List<GenreEntity> getListByKey(String query, String text) throws SQLException {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
        } finally {
            connectionPool.removeToPool(connection);
        }
    }

    private List<GenreEntity> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<GenreEntity> genreEntities = new ArrayList<>();
        while (resultSet.next()) {
            GenreEntity genreEntity = new GenreEntity();
            genreEntity.setId(resultSet.getInt(1));
            genreEntity.setName(resultSet.getString(2));
            genreEntities.add(genreEntity);
        }
        return genreEntities;
    }
}
