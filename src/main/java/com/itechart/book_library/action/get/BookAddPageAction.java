package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookAddPageAction implements Action {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return "add";
    }
}
