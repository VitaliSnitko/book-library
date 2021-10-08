package com.itechart.book_library.servlet;

import com.itechart.book_library.model.dto.BookDto;
import com.itechart.book_library.service.LibraryService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/edit")
@MultipartConfig
public class BookViewEditServlet extends HttpServlet {

    public static RequestDispatcher requestDispatcher;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDto bookDto = null;
        try {
            bookDto = LibraryService.getInstance().getById(Integer.parseInt(req.getParameter("id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("bookDto", bookDto);
        requestDispatcher = req.getRequestDispatcher("WEB-INF/jsp/book-page-edit.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            LibraryService.getInstance().update(new BookDto(req));
            resp.sendRedirect("/");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
