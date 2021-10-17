package com.itechart.book_library.action.post;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BookAddAction implements Action {

    BookService bookService = BookService.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        bookService.createBook(new BookDto(req));

        return "/";
    }
}
