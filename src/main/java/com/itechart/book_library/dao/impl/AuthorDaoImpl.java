package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.AuthorDao;
import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.model.entity.AuthorEntity;
import com.itechart.book_library.model.entity.BookEntity;

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
    private static final String TEMPLATE_FOR_SELECT_BY_BOOK_IDS_QUERY = "SELECT * FROM author JOIN author_book ON author.id = author_book.author_id JOIN book ON author_book.book_id = book.id WHERE book_id IN(?";
    private static String SELECT_BY_BOOK_IDS_QUERY = TEMPLATE_FOR_SELECT_BY_BOOK_IDS_QUERY;

    @Override
    public AuthorEntity create(AuthorEntity authorEntity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, authorEntity.getName());
            statement.execute();
            authorEntity.setId(getIdAfterInserting(statement));
        }
        return authorEntity;
    }

    @Override
    public List<AuthorEntity> getAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            return getListFromResultSet(statement.executeQuery());
        }
    }

    @Override
    public Optional<AuthorEntity> getById(int id) throws SQLException {
        List<AuthorEntity> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public void update(AuthorEntity authorEntity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, authorEntity.getName());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    @Override
    public List<AuthorEntity> getByBookList(List<BookEntity> bookEntityList) throws SQLException {
        for (int i = 1; i < bookEntityList.size(); i++) {
            SELECT_BY_BOOK_IDS_QUERY += ", ?";
        }
        SELECT_BY_BOOK_IDS_QUERY += ")";
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_BOOK_IDS_QUERY)) {
            for (int i = 0; i < bookEntityList.size(); i++) {
                statement.setInt(i + 1, bookEntityList.get(i).getId());
            }
            SELECT_BY_BOOK_IDS_QUERY = TEMPLATE_FOR_SELECT_BY_BOOK_IDS_QUERY;
            return getListFromResultSet(statement.executeQuery());
        }
    }

    private List<AuthorEntity> getListByKey(String query, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        }
    }

    private List<AuthorEntity> getListByKey(String query, String text) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
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
