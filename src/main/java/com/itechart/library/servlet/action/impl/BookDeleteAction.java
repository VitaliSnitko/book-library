package com.itechart.library.servlet.action.impl;

import com.itechart.library.service.BookService;
import com.itechart.library.service.impl.BookServiceProxy;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;
import com.itechart.library.servlet.action.OperationAfterAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookDeleteAction implements Action {

    private final BookService bookService = new BookServiceProxy();

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        String[] ids = req.getParameterValues("delete");
        bookService.delete(ids);
        return new ActionResult(ActionConstants.BOOK_LIST_SOURCE, OperationAfterAction.REDIRECT);
    }
}
