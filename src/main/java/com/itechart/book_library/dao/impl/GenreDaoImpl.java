package com.itechart.book_library.dao.impl;

import com.itechart.book_library.dao.api.BaseDao;
import com.itechart.book_library.dao.api.GenreDao;
import com.itechart.book_library.model.entity.Genre;

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
    public Genre create(Genre genre) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, genre.getName());
            statement.execute();
            genre.setId(getIdAfterInserting(statement));
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            return getListFromResultSet(statement.executeQuery());
        }
    }

    @Override
    public Optional<Genre> getById(int id) throws SQLException {
        List<Genre> rsList = getListByKey(SELECT_BY_ID_QUERY, id);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public void update(Genre genre) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, genre.getName());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Genre> getByName(String name) throws SQLException {
        List<Genre> rsList = getListByKey(SELECT_BY_NAME_QUERY, name);
        return rsList.isEmpty() ? Optional.empty() : Optional.of(rsList.get(0));
    }

    @Override
    public List<Genre> getByBookId(int id) throws SQLException {
        return getListByKey(SELECT_BY_BOOK_ID_QUERY, id);
    }

    private List<Genre> getListByKey(String query, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return getListFromResultSet(statement.executeQuery());
        }
    }

    private List<Genre> getListByKey(String query, String text) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, text);
            return getListFromResultSet(statement.executeQuery());
        }
    }

    private List<Genre> getListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        while (resultSet.next()) {
            Genre genre = new Genre();
            genre.setId(resultSet.getInt(1));
            genre.setName(resultSet.getString(2));
            genres.add(genre);
        }
        return genres;
    }
}
