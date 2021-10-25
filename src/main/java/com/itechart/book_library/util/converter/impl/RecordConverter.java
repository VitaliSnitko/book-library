package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;

public class RecordConverter extends Converter<RecordDto, RecordEntity> {

    private static BookConverter bookConverter = new BookConverter();
    private static ReaderConverter readerConverter = new ReaderConverter();

    public RecordConverter() {
        super(RecordConverter::convertToEntity, RecordConverter::convertToDto);
    }

    private static RecordDto convertToDto(RecordEntity recordEntity) {
        return new RecordDto(
                recordEntity.getId(),
                recordEntity.getBorrowDate(),
                recordEntity.getDueDate(),
                recordEntity.getBookId(),
                readerConverter.toDto(recordEntity.getReader()));
    }

    private static RecordEntity convertToEntity(RecordDto recordDto) {
        return RecordEntity.builder()
                .id(recordDto.getId())
                .borrowDate(recordDto.getBorrowDate())
                .dueDate(recordDto.getDueDate())
                .bookId(recordDto.getBookId())
                .reader(readerConverter.toEntity(recordDto.getReader()))
                .build();
    }

    private static RecordDto convertToDtoFromReq(HttpServletRequest req) {
        LocalDate now = LocalDate.now();
        return RecordDto.builder()
                .borrowDate(Date.valueOf(now))
                .dueDate(Date.valueOf(now.plusMonths(Integer.parseInt(req.getParameter("period")))))
                .bookId(Integer.parseInt(req.getParameter("bookId")))
                .build();
    }
}
