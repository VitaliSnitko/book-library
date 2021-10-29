package com.itechart.library.dao;

import com.itechart.library.model.entity.RecordEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface RecordDao extends Dao<RecordEntity> {
    List<RecordEntity> getRecordsByBookId(int bookId);

    Optional<RecordEntity> getByEmailInCurrentBook(String email, int bookId, Connection connection);
}
