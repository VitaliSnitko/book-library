package com.itechart.book_library.dao.api;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.exception.DaoException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class DaoFactory implements AutoCloseable {

    private ConnectionPool connectionPool;
    private Connection connection;

    private static DaoFactory daoFactory;

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

    @Override
    public void close() {
        connectionPool.closeConnection(connection);
    }
}
