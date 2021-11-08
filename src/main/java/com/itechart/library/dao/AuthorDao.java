package com.itechart.library.dao;

import com.itechart.library.model.entity.AuthorEntity;

import java.util.Optional;

public interface AuthorDao extends Dao<AuthorEntity> {

    Optional<AuthorEntity> getByName(String name);
}
