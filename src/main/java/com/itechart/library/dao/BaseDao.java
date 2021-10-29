package com.itechart.library.dao;

import com.itechart.library.connection.ConnectionPool;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {

    protected ConnectionPool connectionPool = ConnectionPool.getInstance();

    public static <T extends BaseDao> T getDao(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    protected int getIdAfterInserting(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        throw new SQLException();
    }
}
