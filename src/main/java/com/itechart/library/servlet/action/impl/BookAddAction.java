package com.itechart.library.servlet.action.impl;

import com.itechart.library.converter.BookConverter;
import com.itechart.library.converter.impl.BookConverterImpl;
import com.itechart.library.service.BookService;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;
import com.itechart.library.servlet.validator.BookFormValidator;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
public class BookAddAction implements Action {

    private final BookService bookService = BookService.INSTANCE;
    private final BookConverter bookConverter = new BookConverterImpl();
    private final BookFormValidator bookValidator = BookFormValidator.INSTANCE;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (bookValidator.isValid(req)) {
            bookService.create(bookConverter.toDtoFromReq(req));
            return new ActionResult(ActionConstants.BOOK_LIST_PAGE, ActionConstants.redirect);
        } else {
            log.warn("Invalid book parameters caught");
            return new ActionResult(ActionConstants.BOOK_ADD_PAGE, ActionConstants.redirect);
        }
    }
}
