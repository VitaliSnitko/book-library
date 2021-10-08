package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Entity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T extends Entity> {

    T create(T entity) throws SQLException;

    List<T> getAll() throws SQLException;

    Optional<T> getById(int id) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(int id) throws SQLException;

}
