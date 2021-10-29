package com.itechart.library.servlet.validator;

import javax.servlet.http.HttpServletRequest;

public interface Validator {

    boolean isValid(HttpServletRequest req);
}
