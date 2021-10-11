package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.LibraryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class BookEditPageAction implements Action {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        BookDto bookDto = null;
        try {
            bookDto = LibraryService.getInstance().getById(Integer.parseInt(req.getParameter("id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("bookDto", bookDto);
        return "edit";
    }
}
