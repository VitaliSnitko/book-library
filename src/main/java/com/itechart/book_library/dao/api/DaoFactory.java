package com.itechart.book_library.dao.api;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.exception.DaoException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoFactory implements AutoCloseable {
    private ConnectionPool connectionPool;
    private Connection connection;

    public DaoFactory() {
        connectionPool = ConnectionPool.getInstance();
        connection = connectionPool.getConnection();
    }

    public <T extends BaseDao> T getDao(Class<T> clazz) throws DaoException  {
        T t;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
            t.setConnection(connection);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DaoException("Cannot make a new instance of Dao ", e);
        }
        return t;
    }

    public void startTransaction() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        connectionPool.closeConnection(connection);

    }
}
