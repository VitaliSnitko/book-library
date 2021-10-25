package com.itechart.book_library.util.validator;

import javax.servlet.http.HttpServletRequest;

public interface Validator {

    boolean isValid(HttpServletRequest req);
}
