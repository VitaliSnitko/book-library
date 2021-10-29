package com.itechart.library.connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static volatile ConnectionPool connectionPool;

    private BlockingQueue<Connection> connections;
    private String url;
    private String username;
    private String password;
    private String driver;
    private int connectionsLimit;

    private ConnectionPool() {
        loadDBProperties();
        init();
    }

    public static ConnectionPool getInstance() {
        ConnectionPool localInstance = connectionPool;
        if (localInstance == null) {
            synchronized (ConnectionPool.class) {
                localInstance = connectionPool;
                if (localInstance == null) {
                    connectionPool = localInstance = new ConnectionPool();
                }
            }
        }
        return localInstance;
    }

    private void loadDBProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        driver = properties.getProperty("driver");
        connectionsLimit = Integer.parseInt(properties.getProperty("connections.limit"));
    }

    private void init() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connections = new ArrayBlockingQueue<>(connectionsLimit);
        while (connections.size() != connectionsLimit) {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                connections.put(connection);
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        Connection currentConnection = null;
        try {
            currentConnection = connections.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return currentConnection;
    }

    public void returnToPool(Connection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
