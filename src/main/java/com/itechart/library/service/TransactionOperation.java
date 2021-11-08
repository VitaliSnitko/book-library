package com.itechart.library.service;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionOperation {

    void doOperation(Connection connection) throws SQLException;
}
