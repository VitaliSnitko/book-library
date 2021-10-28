package com.itechart.book_library.model.dto;

import com.itechart.book_library.model.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class RecordDto {

    int id;
    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;
    int bookId;
    ReaderDto reader;
    Status status;
}
