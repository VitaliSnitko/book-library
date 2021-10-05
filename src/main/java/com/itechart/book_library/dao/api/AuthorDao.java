package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Author;

import java.util.List;

public interface AuthorDao extends Dao<Author> {
    public Author getByName(String name);
    public List<Author> getByBookId(int id);
}
