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



    @Override
    public void close() {
        connectionPool.removeToPool(connection);
    }
}
