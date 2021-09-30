package com.itechart.book_library.dao;

import com.itechart.book_library.domain.Entity;

import java.sql.ResultSet;
import java.util.List;

public interface Dao<T extends Entity> {

    T create(T entity);

    List<T> getAll();

    List<T> getById(int id);

    void update(T entity);

    void delete(int id);

}
