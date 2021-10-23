package com.itechart.book_library.action.post;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.BookEntity;
import com.itechart.book_library.model.entity.ReaderEntity;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.service.BookService;
import com.itechart.book_library.service.ReaderService;
import com.itechart.book_library.util.converter.Converter;
import com.itechart.book_library.util.converter.impl.BookConverter;
import com.itechart.book_library.util.converter.impl.ReaderConverter;
import com.itechart.book_library.util.converter.impl.RecordConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookEditAction implements Action {

    BookService bookService = BookService.getInstance();
    ReaderService readerService = ReaderService.getInstance();
    private final Converter<BookDto, BookEntity> bookConverter = new BookConverter();


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BookDto bookDto = bookConverter.toDtoFromReq(req);
        bookService.update(bookDto);

        if (req.getParameterValues("email") != null) {
            addRecords(req, bookDto);
        }
        return "/";
    }

    private void addRecords(HttpServletRequest req, BookDto bookDto) {
        readerService.addRecords(
                req.getParameterValues("email"),
                req.getParameterValues("name"),
                req.getParameterValues("period"),
                bookDto.getId());
    }
}
