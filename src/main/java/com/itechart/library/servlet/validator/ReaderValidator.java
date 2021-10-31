package com.itechart.library.servlet.validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public enum ReaderValidator implements Validator {
    INSTANCE;

    private static final String emailPattern = ".*";
    private static final String namePattern = ".+";

    @Override
    public boolean isValid(HttpServletRequest req) {
        return Arrays.stream(req.getParameterValues("email")).allMatch(s -> s.matches(emailPattern))
                && Arrays.stream(req.getParameterValues("name")).allMatch(s -> s.matches(namePattern));
    }
}
