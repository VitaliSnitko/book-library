package com.itechart.library.servlet.validator;

import javax.servlet.http.HttpServletRequest;

public enum BookFormValidator implements Validator {
    INSTANCE;

    private static final String titlePattern = ".+";
    private static final String authorsPattern = ".+ *(, *.+)*";
    private static final String genresPattern = ".+ *(, *.+)*";
    private static final String publisherPattern = ".+";
    private static final String publishDatePattern = ".+";
    private static final String pageCountPattern = "\\d+";
    private static final String ISBNPattern = ".+";
    private static final String totalBookAmountPattern = "\\d+";

    @Override
    public boolean isValid(HttpServletRequest req) {
        return req.getParameter("title").matches(titlePattern)
                && req.getParameter("authors").matches(authorsPattern)
                && req.getParameter("genres").matches(genresPattern)
                && req.getParameter("publisher").matches(publisherPattern)
                && req.getParameter("date").matches(publishDatePattern)
                && req.getParameter("pageCount").matches(pageCountPattern)
                && req.getParameter("ISBN").matches(ISBNPattern)
                && req.getParameter("totalBookAmount").matches(totalBookAmountPattern);
    }
}
