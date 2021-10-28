package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.util.converter.api.ReaderConverter;

import javax.servlet.http.HttpServletRequest;

public class ReaderConverterImpl implements ReaderConverter {

    @Override
    public ReaderEntity toEntity(ReaderDto readerDto) {
        return ReaderEntity.builder()
                .id(readerDto.getId())
                .email(readerDto.getEmail())
                .name(readerDto.getName())
                .build();
    }

    @Override
    public ReaderDto toDto(ReaderEntity readerEntity) {
        return ReaderDto.builder()
                .id(readerEntity.getId())
                .email(readerEntity.getEmail())
                .name(readerEntity.getName())
                .build();
    }

    @Override
    public ReaderDto toDtoFromReq(HttpServletRequest req) {
        return ReaderDto.builder()
                .email(req.getParameter("email"))
                .name(req.getParameter("name"))
                .build();
    }
}
