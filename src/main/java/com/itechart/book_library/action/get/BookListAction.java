package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.service.BookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class BookListAction implements Action {

    private BookService bookService = BookService.getInstance();
    private Properties applicationProperties = new Properties();
    private int bookAmountOnOnePage;

    public BookListAction() {
        try {
            applicationProperties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            bookAmountOnOnePage = Integer.parseInt(applicationProperties.getProperty("book-amount-on-one-page"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        int totalAmountOfBooks = bookService.getBookCount();
        int page = (req.getParameter("page") == null) ? 1 : Integer.parseInt(req.getParameter("page"));
        req.setAttribute("bookList", bookService.getLimitOffset(bookAmountOnOnePage, page));
        req.setAttribute("pageAmount", Math.ceil((float) totalAmountOfBooks / bookAmountOnOnePage));

        return "main";
    }
}
