package com.itechart.library.converter;

import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.entity.BookEntity;

import javax.servlet.http.HttpServletRequest;

public interface BookConverter extends Converter<BookDto, BookEntity>{
    BookDto toDtoFromReq(HttpServletRequest req);
}
