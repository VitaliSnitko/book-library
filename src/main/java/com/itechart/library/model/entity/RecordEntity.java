package com.itechart.library.model.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@SuperBuilder
@Getter
public class RecordEntity extends Entity {

    Date borrowDate;
    Date dueDate;
    Date returnDate;
    BookEntity book;
    ReaderEntity reader;
    Status status;
}
