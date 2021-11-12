package com.itechart.library.servlet.action.impl;

import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.RecordConverter;
import com.itechart.library.converter.impl.BookConverterImpl;
import com.itechart.library.converter.impl.RecordConverterImpl;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.service.impl.BookServiceProxy;
import com.itechart.library.service.impl.ReaderServiceProxy;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;
import com.itechart.library.servlet.action.OperationAfterAction;
import com.itechart.library.servlet.validator.BookFormValidator;
import com.itechart.library.servlet.validator.ReaderValidator;
import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j
public class BookEditAction implements Action {

    private final BookServiceProxy bookService = new BookServiceProxy();
    private final ReaderServiceProxy readerService = new ReaderServiceProxy();
    private final BookConverter bookConverter = new BookConverterImpl();
    private final BookFormValidator bookValidator = new BookFormValidator();
    private final ReaderValidator readerValidator = new ReaderValidator();
    private final RecordConverter recordConverter = new RecordConverterImpl();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {

        if (!bookValidator.isValid(req)) {
            log.warn("Non valid book parameters caught");
            return new ActionResult(ActionConstants.BOOK_EDIT_SOURCE, OperationAfterAction.REDIRECT);
        }

        BookDto bookDto = bookConverter.toDtoFromReq(req);
        bookService.update(bookDto);

        updateRecords(req);

        if (recordsWereAdded(req)) {
            if (readerValidator.isValid(req)) {
                createReaderRecords(req, bookDto);
            } else {
                log.warn("Non valid reader parameters caught");
                return new ActionResult(ActionConstants.BOOK_EDIT_SOURCE, OperationAfterAction.REDIRECT);
            }
        }

        return new ActionResult(ActionConstants.BOOK_LIST_SOURCE, OperationAfterAction.REDIRECT);
    }

    private void updateRecords(HttpServletRequest req) {
        if (req.getParameterValues("status") != null) {
            readerService.updateRecords(recordConverter.toDtosFromReq(req));
        }
    }

    private void createReaderRecords(HttpServletRequest req, BookDto bookDto) {
        readerService.createReaderRecords(
                req.getParameterValues("email"),
                req.getParameterValues("name"),
                req.getParameterValues("period"),
                bookDto);
    }

    private boolean recordsWereAdded(HttpServletRequest req) {
        return req.getParameterValues("email") != null && req.getParameterValues("name") != null;
    }
}
