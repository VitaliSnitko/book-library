package com.itechart.book_library.model.entity;

import lombok.experimental.SuperBuilder;

import java.sql.Date;

@SuperBuilder
public class RecordEntity extends Entity {

    Date borrowDate;
    Date dueDate;
    int bookId;
    ReaderEntity reader;

    public int getBookId() {
        return bookId;
    }

    public ReaderEntity getReader() {
        return reader;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
