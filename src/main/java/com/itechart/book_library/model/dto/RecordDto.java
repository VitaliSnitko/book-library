package com.itechart.book_library.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;

@Builder
@Getter
public class RecordDto {

    int id;
    Date borrowDate;
    Date dueDate;
    int bookId;
    ReaderDto reader;
}
