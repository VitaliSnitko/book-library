package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.ReaderEntity;

import java.util.Optional;

public interface ReaderDao extends Dao<ReaderEntity>{
    Optional<ReaderEntity> getByEmail(String email);
}
