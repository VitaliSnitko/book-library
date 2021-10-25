package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionConstants;
import com.itechart.book_library.action.api.ActionResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SearchPageAction implements Action {

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return new ActionResult(ActionConstants.SEARCH_PAGE);
    }
}
