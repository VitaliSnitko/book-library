package com.itechart.library.converter.impl;

import com.itechart.library.converter.ReaderConverter;
import com.itechart.library.model.dto.ReaderDto;
import com.itechart.library.model.entity.ReaderEntity;

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
