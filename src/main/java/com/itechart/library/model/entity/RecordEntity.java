package com.itechart.library.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecordEntity extends Entity {

    Date borrowDate;
    Date dueDate;
    Date returnDate;
    BookEntity book;
    ReaderEntity reader;
    Status status;
}
