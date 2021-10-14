package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.LibraryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class BookEditPageAction implements Action {

    private LibraryService libraryService = LibraryService.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        BookDto bookDto = libraryService.getById(Integer.parseInt(req.getParameter("id")));
        req.setAttribute("bookDto", bookDto);
        return "edit";
    }
}
