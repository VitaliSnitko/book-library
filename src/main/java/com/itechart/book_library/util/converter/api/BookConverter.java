package com.itechart.book_library.util.converter.api;

import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.entity.BookEntity;

import javax.servlet.http.HttpServletRequest;

public interface BookConverter extends Converter<BookDto, BookEntity>{
    BookDto toDtoFromReq(HttpServletRequest req);
}
