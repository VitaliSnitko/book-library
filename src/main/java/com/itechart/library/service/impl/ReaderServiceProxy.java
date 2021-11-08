package com.itechart.library.service.impl;

import com.itechart.library.connection.ConnectionPool;
import com.itechart.library.model.dto.BookDto;
import com.itechart.library.model.dto.RecordDto;
import com.itechart.library.service.TransactionHandler;

import java.util.List;

public class ReaderServiceProxy extends ReaderServiceImpl {

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    protected final TransactionHandler transactionHandler = new TransactionHandler(connectionPool);

    public ReaderServiceProxy() {
        super();
    }

    public void createReaderRecords(String[] emails, String[] names, String[] periods, BookDto bookDto) {
        transactionHandler.doTransaction((connection) -> {
            super.createReaderRecords(emails, names, periods, bookDto, connection);
        });
    }

    public void updateRecords(List<RecordDto> recordDtos) {
        transactionHandler.doTransaction((connection) -> {
            super.updateRecords(recordDtos, connection);
        });
    }
}
