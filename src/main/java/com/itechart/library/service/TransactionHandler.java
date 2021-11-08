package com.itechart.library.service;

import com.itechart.library.connection.ConnectionPool;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.SQLException;

@Log4j
public class TransactionHandler {

    private final ConnectionPool connectionPool;

    public TransactionHandler(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void doTransaction(TransactionOperation transactionOperation) {
        Connection connection = connectionPool.getConnection();
        setAutoCommit(connection, false);

        try {
            transactionOperation.doOperation(connection);
            commit(connection);
        } catch (SQLException e) {
            rollback(connection);
        } finally {
            setAutoCommit(connection, true);
            connectionPool.returnToPool(connection);
        }
    }

    public void setAutoCommit(Connection connection, boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            log.error(e);
        }
    }

    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    public void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error(e);
        }
    }
}
