package com.itechart.library.servlet.action.impl;

import com.itechart.library.service.BookService;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookDeleteAction implements Action {

    private final BookService bookService = BookService.INSTANCE;

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] ids = req.getParameterValues("delete");
        bookService.delete(ids);
        return new ActionResult(ActionConstants.BOOK_LIST_PAGE, ActionConstants.redirect);
    }
}
