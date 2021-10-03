package com.itechart.book_library.dao.api;

import java.sql.Connection;

public abstract class BaseDao {
    protected Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
