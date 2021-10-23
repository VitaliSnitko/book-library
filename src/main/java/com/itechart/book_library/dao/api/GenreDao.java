package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.GenreEntity;

import java.util.List;
import java.util.Optional;

public interface GenreDao extends Dao<GenreEntity> {
    Optional<GenreEntity> getByName(String name);

    List<GenreEntity> getByBookId(int id);
}
