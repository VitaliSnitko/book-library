package com.itechart.library.dao;

import lombok.extern.log4j.Log4j;

import java.lang.reflect.InvocationTargetException;

@Log4j
public class DaoFactory {

    public static <T extends BaseDao> T getDao(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            log.error(e);
        }
        return t;
    }
}
