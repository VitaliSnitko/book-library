package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.Author;

public interface AuthorDao extends Dao<Author> {
    public Author getByName(String name);
}
