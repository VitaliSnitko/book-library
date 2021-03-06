package com.itechart.library.servlet.action.impl;

import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.dto.ReaderDto;
import com.itechart.library.model.dto.RecordDto;
import com.itechart.library.model.entity.Status;
import com.itechart.library.service.BookService;
import com.itechart.library.service.impl.BookServiceProxy;
import com.itechart.library.service.impl.ReaderServiceProxy;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

public class BookPageAction implements Action {

    public static final int DEFAULT_BOOK_ID_IF_REQ_PARAM_INVALID = 0;
    private final BookService bookService = new BookServiceProxy();
    private final ReaderServiceProxy readerService = new ReaderServiceProxy();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        BookDto bookDto = bookService.getById(getBookId(req));
        List<RecordDto> records = readerService.getRecordsByBookId(getBookId(req));

        req.setAttribute("activeRecords", getActiveRecords(records));
        req.setAttribute("inactiveRecords", getInactiveRecords(records));
        req.setAttribute("emailSuggestions", readerService.getAllReaders().stream().map(ReaderDto::getEmail).toArray());
        req.setAttribute("nameSuggestions", readerService.getAllReaders().stream().map(ReaderDto::getName).toArray());
        req.setAttribute("nearestAvailableDate", readerService.getNearestAvailableDate(bookDto));
        req.setAttribute("bookDto", bookDto);
        return new ActionResult(ActionConstants.BOOK_EDIT_SOURCE);
    }

    private int getBookId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return DEFAULT_BOOK_ID_IF_REQ_PARAM_INVALID;
        }
    }

    private List<RecordDto> getActiveRecords(List<RecordDto> records) {
        return records.stream().filter(r -> r.getStatus() == Status.BORROWED).collect(Collectors.toList());
    }

    private List<RecordDto> getInactiveRecords(List<RecordDto> records) {
        return records.stream().filter(r -> r.getStatus() != Status.BORROWED).collect(Collectors.toList());
    }
}
