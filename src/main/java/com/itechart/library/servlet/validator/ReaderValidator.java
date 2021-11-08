package com.itechart.library.servlet.validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public enum ReaderValidator implements Validator {
    INSTANCE;

    @Override
    public boolean isValid(HttpServletRequest req) {
        return req.getParameterValues("email") != null
                && req.getParameterValues("name") != null
                && Arrays.stream(req.getParameterValues("email")).noneMatch(String::isBlank)
                && Arrays.stream(req.getParameterValues("name")).noneMatch(String::isBlank);
    }
}
