package com.itechart.library.servlet.action.impl;

import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.service.BookService;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class BookListAction implements Action {

    private final BookService bookService = BookService.INSTANCE;
    private int pageBookAmount;
    private static final int DEFAULT_PAGE_NUM = 1;

    public BookListAction() {
        try {
            Properties applicationProperties = new Properties();
            applicationProperties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            this.pageBookAmount = Integer.parseInt(applicationProperties.getProperty("book-amount-on-one-page"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = getPage(req);
        BookSpecification specification = getSpecification(req);
        int totalBookAmount = bookService.getBookCountBySpecification(specification);

        req.setAttribute("bookList", bookService.getLimitOffsetBySpecification(specification, this.pageBookAmount, page));
        req.setAttribute("pageAmount", Math.ceil((float) totalBookAmount / this.pageBookAmount));
        return new ActionResult(ActionConstants.BOOK_LIST_PAGE);
    }

    private int getPage(HttpServletRequest req) {
        return (req.getParameter("page") == null) ? DEFAULT_PAGE_NUM : Integer.parseInt(req.getParameter("page"));
    }

    private BookSpecification getSpecification(HttpServletRequest req) {
        return BookSpecification.builder()
                .title(req.getParameter("title"))
                .authors(req.getParameter("authors"))
                .genres(req.getParameter("genres"))
                .description(req.getParameter("description"))
                .build()
                .convertSpecificationParametersToRegex();
    }
}
