package com.itechart.book_library.dao.api;

import com.itechart.book_library.connection.ConnectionPool;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class DaoConnectionProvider implements AutoCloseable {

    private ConnectionPool connectionPool;
    private Connection connection;

    public DaoConnectionProvider() {
        connectionPool = ConnectionPool.getInstance();
        connection = connectionPool.getConnection();
    }

    public <T extends BaseDao> T getDao(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
            t.setConnection(connection);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public void close() {
        connectionPool.closeConnection(connection);
    }
}
