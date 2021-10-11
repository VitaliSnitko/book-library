package com.itechart.book_library.dao.api;

import com.itechart.book_library.connection.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {
    protected ConnectionPool connectionPool = ConnectionPool.getInstance();

    protected int getIdAfterInserting(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        throw new SQLException();
    }
}
