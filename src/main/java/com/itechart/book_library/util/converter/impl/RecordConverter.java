package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.util.converter.Converter;

public class RecordConverter extends Converter<RecordDto, RecordEntity> {

    private static final ReaderConverter readerConverter = new ReaderConverter();

    public RecordConverter() {
        super(RecordConverter::convertToEntity, RecordConverter::convertToDto);
    }

    private static RecordDto convertToDto(RecordEntity recordEntity) {
        return RecordDto.builder()
                .id(recordEntity.getId())
                .borrowDate(recordEntity.getBorrowDate())
                .dueDate(recordEntity.getDueDate())
                .bookId(recordEntity.getBookId())
                .reader(readerConverter.toDto(recordEntity.getReader()))
                .build();
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
}
