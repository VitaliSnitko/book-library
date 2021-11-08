package com.itechart.library.servlet.action.impl;

import com.itechart.library.dao.criteria.BookSpecification;
import com.itechart.library.service.BookService;
import com.itechart.library.service.impl.BookServiceProxy;
import com.itechart.library.servlet.action.Action;
import com.itechart.library.servlet.action.ActionConstants;
import com.itechart.library.servlet.action.ActionResult;
import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

@Log4j
public class BookListAction implements Action {

    public static final int DEFAULT_PAGE_IF_REQ_PARAM_INVALID = 0;
    private final BookService bookService = new BookServiceProxy();
    private int pageBookAmount;
    private static final int DEFAULT_PAGE = 1;

    public BookListAction() {
        try {
            Properties applicationProperties = new Properties();
            applicationProperties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            this.pageBookAmount = Integer.parseInt(applicationProperties.getProperty("book-amount-on-one-page"));
        } catch (IOException e) {
            log.error(e);
        }
    }

    @Override
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = getPage(req);
        BookSpecification specification = getSpecification(req);
        int totalBookAmount = bookService.getBookCount(specification);

        req.setAttribute("bookList", page == DEFAULT_PAGE_IF_REQ_PARAM_INVALID
                ? new ArrayList<>()
                : bookService.getLimitOffsetBySpecification(specification, this.pageBookAmount, page));
        req.setAttribute("pageAmount", Math.ceil((float) totalBookAmount / this.pageBookAmount));
        return new ActionResult(ActionConstants.BOOK_LIST_PAGE);
    }

    private int getPage(HttpServletRequest req) {
        if (req.getParameter("page") == null) {
            return DEFAULT_PAGE;
        }
        try {
            return Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_IF_REQ_PARAM_INVALID;
        }
    }

    private BookSpecification getSpecification(HttpServletRequest req) {
        return BookSpecification.builder()
                .title(req.getParameter("title"))
                .authors(req.getParameter("authors"))
                .genres(req.getParameter("genres"))
                .description(req.getParameter("description"))
                .onlyAvailable(req.getParameter("onlyAvailable") != null)
                .build()
                .convertSpecificationParametersToRegex();
    }
}
