package com.itechart.book_library.action.get;

import com.itechart.book_library.action.api.Action;
import com.itechart.book_library.action.api.ActionConstants;
import com.itechart.book_library.action.api.ActionResult;
import com.itechart.book_library.dao.criteria.BookSpecification;
import com.itechart.book_library.service.BookService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class BookListAction implements Action {

    private BookService bookService = BookService.INSTANCE;
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
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp) {

        int page = getPage(req);
        BookSpecification specification = getSpecification(req);
        int totalAmountOfBooks = bookService.getBookCountBySpecification(specification);

        req.setAttribute("bookList", bookService.getLimitOffsetBySpecification(specification, bookAmountOnOnePage, page));
        req.setAttribute("pageAmount", Math.ceil((float) totalAmountOfBooks / bookAmountOnOnePage));
        return new ActionResult(ActionConstants.BOOK_LIST_PAGE);
    }

    private int getPage(HttpServletRequest req) {
        return (req.getParameter("page") == null) ? 1 : Integer.parseInt(req.getParameter("page"));
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
