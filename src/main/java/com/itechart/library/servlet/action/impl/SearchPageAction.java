package com.itechart.library.servlet.action.impl;

import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;

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
