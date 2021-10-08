package com.itechart.book_library.servlet;

import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.LibraryService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("")
public class MainPageServlet extends HttpServlet {

    public static RequestDispatcher requestDispatcher;
    //LibraryService getInstance
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<BookDto> bookDtoList = null;
        try {
            bookDtoList = LibraryService.getInstance().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("bookList", bookDtoList);
        requestDispatcher = req.getRequestDispatcher("index.jsp");
        requestDispatcher.forward(req, resp);

    }

}
