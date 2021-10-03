package com.itechart.book_library.dao.api;

import com.itechart.book_library.entity.Entity;

import java.util.List;

public interface Dao<T extends Entity> {

    T create(T entity);

    List<T> getAll();

    T getById(int id);

    void update(T entity);

    void delete(int id);

}
