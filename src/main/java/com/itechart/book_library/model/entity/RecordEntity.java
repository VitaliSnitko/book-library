package com.itechart.book_library.model.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@SuperBuilder
@Getter
public class RecordEntity extends Entity {

    Date borrowDate;
    Date dueDate;
    Date returnDate;
    int bookId;
    ReaderEntity reader;
    Status status;
}
