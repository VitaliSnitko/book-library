package com.itechart.library.servlet.validator;

import javax.servlet.http.HttpServletRequest;

public class BookFormValidator implements Validator {

    private static final String AUTHORS_PATTERN = ".+ *(, *.+)*";
    private static final String GENRES_PATTERN = ".+ *(, *.+)*";
    private static final String PAGE_COUNT_PATTERN = "\\d+";
    private static final String TOTAL_BOOK_AMOUNT_PATTERN = "\\d+";

    @Override
    public boolean isValid(HttpServletRequest req) {
        return req.getParameter("title") != null
                && req.getParameter("authors") != null
                && req.getParameter("genres") != null
                && req.getParameter("publisher") != null
                && req.getParameter("date") != null
                && req.getParameter("pageCount") != null
                && req.getParameter("ISBN") != null
                && req.getParameter("totalBookAmount") != null
                && !req.getParameter("title").isBlank()
                && req.getParameter("authors").matches(AUTHORS_PATTERN)
                && req.getParameter("genres").matches(GENRES_PATTERN)
                && !req.getParameter("publisher").isBlank()
                && !req.getParameter("date").isBlank()
                && req.getParameter("pageCount").matches(PAGE_COUNT_PATTERN)
                && !req.getParameter("ISBN").isBlank()
                && req.getParameter("totalBookAmount").matches(TOTAL_BOOK_AMOUNT_PATTERN);
    }
}
