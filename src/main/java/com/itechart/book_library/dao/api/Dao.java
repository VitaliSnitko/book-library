package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface Dao<T extends Entity> {

    T create(T entity, Connection connection) throws SQLException;

    Optional<T> getById(int id);

    void update(T entity, Connection connection);

    void delete(Integer[] ids);

}
