package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.GenreDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.GenreEntity;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.util.converter.Converter;

import java.util.function.Function;

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
        return new RecordEntity(
                recordDto.getId(),
                recordDto.getBorrowDate(),
                recordDto.getDueDate(),
                recordDto.getBookId(),
                readerConverter.toEntity(recordDto.getReader()));
    }
}
