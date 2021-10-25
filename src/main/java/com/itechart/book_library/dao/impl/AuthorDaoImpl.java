package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.AuthorDao;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.model.entity.AuthorEntity;
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
    private static final String SELECT_ALL_QUERY = "SELECT * FROM author";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM author WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM author WHERE name = ?";
    private static final String UPDATE_QUERY = "UPDATE author SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM author WHERE id = ?";
    private static final String SELECT_BY_BOOK_ID_QUERY = "SELECT * FROM author JOIN author_book ON author.id = author_book.author_id JOIN book ON author_book.book_id = book.id WHERE book_id = ?";

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
    public Optional<AuthorEntity> getByName(String name) {
        List<AuthorEntity> rsList = getListByKey(SELECT_BY_NAME_QUERY, name);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public List<AuthorEntity> getByBookId(int id) {
        return getListByKey(SELECT_BY_BOOK_ID_QUERY, id);
    }

    private List<AuthorEntity> getListByKey(String query, int id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            log.error("Cannot get list by " + id + " key ", e);
            return null;
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
            log.error("Cannot get list by " + text + " key ", e);
            return null;
        } finally {
            connectionPool.returnToPool(connection);
        }
    }

    private List<AuthorEntity> getListFromResultSet(ResultSet resultSet) {
        List<AuthorEntity> authorEntities = new ArrayList<>();
        try {
            while (resultSet.next()) {
                AuthorEntity authorEntity = new AuthorEntity();
                authorEntity.setId(resultSet.getInt(1));
                authorEntity.setName(resultSet.getString(2));
                authorEntities.add(authorEntity);
            }
        } catch (SQLException e) {
            log.error("Result set error ", e);
        }
        return authorEntities;
    }
}
