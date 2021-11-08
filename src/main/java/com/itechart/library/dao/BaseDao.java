package com.itechart.library.dao;

import com.itechart.library.connection.ConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {

    protected ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * Serves for getting id of entity after inserting entity to database.
     * (Due to "RETURNING" SQL operator)
     * @return entity id
     */
    protected int getIdAfterInserting(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        throw new SQLException();
    }
}
