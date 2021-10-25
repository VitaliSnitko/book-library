package com.itechart.book_library.model.dto;

import lombok.Builder;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;

@Builder
public class RecordDto {

    int id;
    Date borrowDate;
    Date dueDate;
    int bookId;
    ReaderDto reader;

    public RecordDto(int id, Date borrowDate, Date dueDate, int bookId, ReaderDto reader) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.bookId = bookId;
        this.reader = reader;
    }

    public RecordDto(HttpServletRequest req) {
        LocalDate now = LocalDate.now();
        this.borrowDate = Date.valueOf(now);
        this.dueDate = Date.valueOf(now.plusMonths(Integer.parseInt(req.getParameter("period"))));
        this.bookId = Integer.parseInt(req.getParameter("bookId"));
    }

    public int getId() {
        return id;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getBookId() {
        return bookId;
    }

    public ReaderDto getReader() {
        return reader;
    }

    public void setReader(ReaderDto reader) {
        this.reader = reader;
    }
}
