package com.itechart.book_library.action.post;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionConstants;
import com.itechart.book_library.action.api.ActionResult;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.BookService;
import com.itechart.book_library.service.ReaderService;
import com.itechart.book_library.util.converter.api.BookConverter;
import com.itechart.book_library.util.converter.api.RecordConverter;
import com.itechart.book_library.util.converter.impl.BookConverterImpl;
import com.itechart.book_library.util.converter.impl.RecordConverterImpl;
import com.itechart.book_library.util.validator.BookFormValidator;
import com.itechart.book_library.util.validator.ReaderValidator;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
public class BookEditAction implements Action {

    BookService bookService = BookService.INSTANCE;
    ReaderService readerService = ReaderService.INSTANCE;
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
                addRecords(req, bookDto);
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

    private void addRecords(HttpServletRequest req, BookDto bookDto) {
        readerService.addRecords(
                req.getParameterValues("email"),
                req.getParameterValues("name"),
                req.getParameterValues("period"),
                bookDto.getId());
    }

    private boolean recordsWereAdded(HttpServletRequest req) {
        return req.getParameterValues("email") != null && req.getParameterValues("name") != null;
    }
}
