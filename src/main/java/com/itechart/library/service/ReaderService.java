package com.itechart.library.service;

import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.dto.ReaderDto;
import com.itechart.library.model.dto.RecordDto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReaderService {

    void createReaderRecords(String[] emails, String[] names, String[] periods, BookDto bookDto, Connection connection) throws SQLException;

    void updateRecords(List<RecordDto> recordDtos, Connection connection) throws SQLException;

    Date getNearestAvailableDate(BookDto bookDto);

    List<RecordDto> getRecordsByBookId(int bookId);

    List<ReaderDto> getAllReaders();

    List<ReaderDto> getReadersByEmailInput(String email);
}
