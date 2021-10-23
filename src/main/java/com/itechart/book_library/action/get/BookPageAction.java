package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.service.BookService;
import com.itechart.book_library.service.ReaderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

public class BookPageAction implements Action {

    private final BookService bookService = BookService.getInstance();
    private final ReaderService readerService = ReaderService.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        BookDto bookDto = bookService.getById(Integer.parseInt(req.getParameter("id")));
        List<RecordDto> records = readerService.getRecords(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("records", records);
        req.setAttribute("suggestions", readerService.getAllReaders().stream().map(ReaderDto::getEmail).toArray());
        req.setAttribute("bookDto", bookDto);
        return "edit";
    }
}
