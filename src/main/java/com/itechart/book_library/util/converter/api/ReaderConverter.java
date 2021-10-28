package com.itechart.book_library.util.converter.api;

import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.entity.ReaderEntity;

import javax.servlet.http.HttpServletRequest;

public interface ReaderConverter extends Converter<ReaderDto, ReaderEntity> {
    ReaderDto toDtoFromReq(HttpServletRequest req);
}
