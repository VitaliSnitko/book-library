package com.itechart.book_library.model.entity;

import java.sql.Date;

public class RecordEntity extends Entity{
    Date borrowDate;
    Date dueDate;
    int bookId;
    ReaderEntity reader;

    public RecordEntity() {
    }

    public RecordEntity(int id, Date borrowDate, Date dueDate, int bookId, ReaderEntity reader) {
        super(id);
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.bookId = bookId;
        this.reader = reader;
    }

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
