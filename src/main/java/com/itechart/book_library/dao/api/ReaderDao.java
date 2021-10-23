package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.ReaderEntity;

import java.util.List;
import java.util.Optional;

public interface ReaderDao extends Dao<ReaderEntity> {

    List<ReaderEntity> getAll();

    Optional<ReaderEntity> getByEmail(String email);

    Optional<ReaderEntity> getByEmailInCurrentBook(String email, int bookId);
}
