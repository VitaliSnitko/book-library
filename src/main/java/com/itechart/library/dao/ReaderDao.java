package com.itechart.library.dao;

import com.itechart.library.model.entity.ReaderEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ReaderDao extends Dao<ReaderEntity> {

    List<ReaderEntity> getAll();

    List<ReaderEntity> getByEmailInput(String email);

    Optional<ReaderEntity> getByEmail(String email, Connection connection);
}
