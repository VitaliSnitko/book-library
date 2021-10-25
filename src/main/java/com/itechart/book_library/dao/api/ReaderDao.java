package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.ReaderEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ReaderDao extends Dao<ReaderEntity> {

    List<ReaderEntity> getAll();

    Optional<ReaderEntity> getByEmail(String email, Connection connection);

    Optional<ReaderEntity> getByEmailInCurrentBook(String email, int bookId, Connection connection);
}
