package com.itechart.library.converter;

import com.itechart.library.model.dto.ReaderDto;
import com.itechart.library.model.entity.ReaderEntity;

import javax.servlet.http.HttpServletRequest;

public interface ReaderConverter extends Converter<ReaderDto, ReaderEntity> {
    ReaderDto toDtoFromReq(HttpServletRequest req);
}
