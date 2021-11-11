package com.itechart.library.servlet.validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class ReaderValidator implements Validator {

    @Override
    public boolean isValid(HttpServletRequest req) {
        return req.getParameterValues("email") != null
                && req.getParameterValues("name") != null
                && req.getParameterValues("email").length == req.getParameterValues("name").length
                && Arrays.stream(req.getParameterValues("email")).noneMatch(String::isBlank)
                && Arrays.stream(req.getParameterValues("name")).noneMatch(String::isBlank);
    }
}
