package com.itechart.library.dao;

import com.itechart.library.model.entity.GenreEntity;

import java.util.Optional;

public interface GenreDao extends Dao<GenreEntity> {

    Optional<GenreEntity> getByName(String name);

}
