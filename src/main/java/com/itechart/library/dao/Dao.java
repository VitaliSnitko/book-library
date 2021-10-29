package com.itechart.library.dao;

import com.itechart.library.model.entity.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface Dao<T extends Entity> {

    T create(T entity, Connection connection) throws SQLException;

    Optional<T> getById(int id);

    void update(T entity, Connection connection) throws SQLException;

    void delete(Integer[] ids);

    void delete(Integer[] ids, Connection connection) throws SQLException;

}
