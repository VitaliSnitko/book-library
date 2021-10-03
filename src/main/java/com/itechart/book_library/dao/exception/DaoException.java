package com.itechart.book_library.dao.exception;

public class DaoException extends Exception {
    public DaoException(String message, Exception e) {
        super(message, e);
    }

    public DaoException(String message) {
        super(message);
    }
}
