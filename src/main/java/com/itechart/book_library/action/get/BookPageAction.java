package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionConstants;
import com.itechart.book_library.action.api.ActionResult;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.service.BookService;
import com.itechart.book_library.service.ReaderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BookPageAction implements Action {

    private final BookService bookService = BookService.INSTANCE;
    private final ReaderService readerService = ReaderService.INSTANCE;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        BookDto bookDto = bookService.getById(Integer.parseInt(req.getParameter("id")));
        List<RecordDto> activeRecords = readerService.getRecords(Integer.parseInt(req.getParameter("id")), true);
        List<RecordDto> inactiveRecords = readerService.getRecords(Integer.parseInt(req.getParameter("id")), false);

        req.setAttribute("activeRecords", activeRecords);
        req.setAttribute("inactiveRecords", inactiveRecords);
        req.setAttribute("emailSuggestions", readerService.getAllReaders().stream().map(ReaderDto::getEmail).toArray());
        req.setAttribute("nameSuggestions", readerService.getAllReaders().stream().map(ReaderDto::getName).toArray());
        req.setAttribute("bookDto", bookDto);
        return new ActionResult(ActionConstants.BOOK_EDIT_PAGE);
    }
}
