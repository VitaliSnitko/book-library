package com.itechart.library.servlet.action.impl;

import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.RecordConverter;
import com.itechart.library.converter.impl.BookConverterImpl;
import com.itechart.library.converter.impl.RecordConverterImpl;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.service.BookService;
import com.itechart.library.service.ReaderService;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;
import com.itechart.library.servlet.validator.BookFormValidator;
import com.itechart.library.servlet.validator.ReaderValidator;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
public class BookEditAction implements Action {

    private final BookService bookService = BookService.INSTANCE;
    private final ReaderService readerService = ReaderService.INSTANCE;
    private final BookConverter bookConverter = new BookConverterImpl();
    private final BookFormValidator bookValidator = BookFormValidator.INSTANCE;
    private final ReaderValidator readerValidator = ReaderValidator.INSTANCE;
    private final RecordConverter recordConverter = new RecordConverterImpl();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!bookValidator.isValid(req)) {
            log.warn("Non valid book parameters caught");
            return new ActionResult(ActionConstants.BOOK_EDIT_PAGE, ActionConstants.redirect);
        }

        BookDto bookDto = bookConverter.toDtoFromReq(req);
        bookService.update(bookDto);

        updateRecords(req);

        if (recordsWereAdded(req)) {
            if (readerValidator.isValid(req)) {
                saveRecords(req, bookDto);
            } else {
                log.warn("Non valid reader parameters caught");
                return new ActionResult(ActionConstants.BOOK_EDIT_PAGE, ActionConstants.redirect);
            }
        }

        return new ActionResult(ActionConstants.BOOK_LIST_PAGE, ActionConstants.redirect);
    }

    private void updateRecords(HttpServletRequest req) {
        if (req.getParameterValues("recordId") != null) {
            readerService.updateRecords(recordConverter.toDtosFromReq(req));
        }
    }

    private void saveRecords(HttpServletRequest req, BookDto bookDto) {
        readerService.saveRecords(
                req.getParameterValues("email"),
                req.getParameterValues("name"),
                req.getParameterValues("period"),
                bookDto);
    }

    private boolean recordsWereAdded(HttpServletRequest req) {
        return req.getParameterValues("email") != null && req.getParameterValues("name") != null;
    }
}
