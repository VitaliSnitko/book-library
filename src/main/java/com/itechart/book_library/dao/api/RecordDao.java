package com.itechart.book_library.dao.api;

import com.itechart.book_library.model.entity.RecordEntity;

import java.util.List;

public interface RecordDao extends Dao<RecordEntity> {
    List<RecordEntity> getRecordsByBookId(int bookId, boolean areRecordsActive);
}
