package com.itechart.library.dao;

import com.itechart.library.model.entity.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Provides CRUD operation for all entities.
 * Methods that assume {@link Connection} parameter are serve
 * for managing transactions
 */
public interface Dao<T extends Entity> {

    T create(T entity, Connection connection) throws SQLException;

    Optional<T> getById(int id);

    T update(T entity, Connection connection) throws SQLException;

    void delete(Integer[] ids);

    void delete(Integer[] ids, Connection connection) throws SQLException;
}
