package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionConstants;
import com.itechart.book_library.action.api.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookAddPageAction implements Action {

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        return new ActionResult(ActionConstants.BOOK_ADD_PAGE);
    }
}
