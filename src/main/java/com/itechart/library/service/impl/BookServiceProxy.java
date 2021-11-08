package com.itechart.library.service.impl;

import com.itechart.library.connection.ConnectionPool;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.service.TransactionHandler;

public class BookServiceProxy extends BookServiceImpl {

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    protected final TransactionHandler transactionHandler = new TransactionHandler(connectionPool);

    public BookServiceProxy() {
        super();
    }

    public void create(BookDto bookDto) {
        transactionHandler.doTransaction((connection) -> {
            super.create(bookDto, connection);
        });
    }

    public void update(BookDto bookDto) {
        transactionHandler.doTransaction((connection) -> {
            super.update(bookDto, connection);
        });
    }
}
