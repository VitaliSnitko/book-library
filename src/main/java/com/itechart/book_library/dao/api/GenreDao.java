package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.GenreEntity;

import java.util.Optional;

public interface GenreDao extends Dao<GenreEntity> {

    Optional<GenreEntity> getByName(String name);

}
