package com.itechart.book_library.service;

import com.itechart.book_library.dao.api.BaseDao;

import java.lang.reflect.InvocationTargetException;

public class Service {
    protected static <T extends BaseDao> T getDao(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}
