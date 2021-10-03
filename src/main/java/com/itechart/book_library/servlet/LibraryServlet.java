package com.itechart.book_library.servlet;

import com.itechart.book_library.entity.Book;
import com.itechart.book_library.service.BookService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/add")
@MultipartConfig
public class LibraryServlet extends HttpServlet {

    public static RequestDispatcher requestDispatcher;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        requestDispatcher = req.getRequestDispatcher("WEB-INF/jsp/add.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        BookService bookService = new BookService();

        String title       = req.getParameter("title");
        String[] authors   = req.getParameter("authors").split(" *, *");
        String[] genres    = req.getParameter("genres").split(" *, *");
        String publisher   = req.getParameter("publisher");
        System.out.println(req.getParameter("date"));
        Date publishDate   = getDate(req.getParameter("date"));
        Integer pageCount  = Integer.parseInt(req.getParameter("page count"));
        String ISBN        = req.getParameter("ISBN");
        String description = req.getParameter("description");
        InputStream cover  = req.getPart("cover").getInputStream();
        Book book = new Book();
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setPublishDate(publishDate);
        book.setPageCount(pageCount);
        book.setISBN(ISBN);
        book.setDescription(description);
        book.setCover(cover);
        bookService.createBook(book, authors, genres);
        resp.sendRedirect("/");
    }

    private Date getDate(String date) {
        java.util.Date utilDate = null;
        try {
            utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(utilDate.getTime());
    }

}
