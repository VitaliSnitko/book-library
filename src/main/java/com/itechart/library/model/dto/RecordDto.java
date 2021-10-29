package com.itechart.library.model.dto;

import com.itechart.library.model.entity.Status;
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
    BookDto book;
    ReaderDto reader;
    Status status;
}
