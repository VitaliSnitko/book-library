package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.util.converter.Converter;

import javax.servlet.http.HttpServletRequest;

public class ReaderConverter extends Converter<ReaderDto, ReaderEntity> {

    public ReaderConverter() {
        super(ReaderConverter::convertToEntity, ReaderConverter::convertToDto, ReaderConverter::convertToDtoFromReq);
    }

    private static ReaderDto convertToDto(ReaderEntity readerEntity) {
        return ReaderDto.builder()
                .id(readerEntity.getId())
                .email(readerEntity.getEmail())
                .name(readerEntity.getName())
                .build();
    }

    private static ReaderEntity convertToEntity(ReaderDto readerDto) {
        return ReaderEntity.builder()
                .id(readerDto.getId())
                .email(readerDto.getEmail())
                .name(readerDto.getName())
                .build();
    }

    private static ReaderDto convertToDtoFromReq(HttpServletRequest req) {
        return ReaderDto.builder()
                .email(req.getParameter("email"))
                .name(req.getParameter("name"))
                .build();
    }
}
