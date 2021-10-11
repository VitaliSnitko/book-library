package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.LibraryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public class MainPageAction implements Action {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<BookDto> bookDtoList = null;
        try {
            bookDtoList = LibraryService.getInstance().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("bookList", bookDtoList);
        return "";
    }
}
