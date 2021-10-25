package com.itechart.book_library.util.validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public enum ReaderValidator implements Validator {
    INSTANCE;

    private static final String emailPattern = "[a-zA-Z]+@[a-zA-Z]+\\.*[a-zA-Z]*";
    private static final String namePattern = ".+";

    @Override
    public boolean isValid(HttpServletRequest req) {
        return Arrays.stream(req.getParameterValues("email")).allMatch(s -> s.matches(emailPattern))
                && Arrays.stream(req.getParameterValues("name")).allMatch(s -> s.matches(namePattern));
    }
}
