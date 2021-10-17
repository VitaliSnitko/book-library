package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.util.converter.Converter;

public class ReaderConverter extends Converter<ReaderDto, ReaderEntity> {

    private static RecordConverter recordConverter = new RecordConverter();

    public ReaderConverter() {
        super(ReaderConverter::convertToEntity, ReaderConverter::convertToDto);
    }

    private static ReaderDto convertToDto(ReaderEntity readerEntity) {
        return new ReaderDto(
                readerEntity.getId(),
                readerEntity.getEmail(),
                readerEntity.getName());
    }

    private static ReaderEntity convertToEntity(ReaderDto readerDto) {
        return new ReaderEntity(
                readerDto.getId(),
                readerDto.getEmail(),
                readerDto.getName());
    }
}
