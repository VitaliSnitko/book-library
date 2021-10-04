package com.itechart.book_library.dao.api;

import com.itechart.book_library.connection.ConnectionPool;
import com.itechart.book_library.dao.exception.DaoException;

import java.lang.reflect.InvocationTargetException;

public class DaoFactory {

    private ConnectionPool connectionPool;

    private static DaoFactory daoFactory;

    private DaoFactory() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static DaoFactory getInstance() {
        if (daoFactory == null)
            daoFactory = new DaoFactory();
        return daoFactory;
    }

    public <T extends BaseDao> T getDao(Class<T> clazz) throws DaoException  {
        T t;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
            t.setConnection(connectionPool.getConnection());
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DaoException("Cannot make a new instance of Dao ", e);
        }
        return t;
    }

}
