package com.itechart.library.model.dto;

import com.itechart.library.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto extends Dto {

    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;
    BookDto book;
    ReaderDto reader;
    Status status;
}
