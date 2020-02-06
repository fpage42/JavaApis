package fr.fpage.conservatoireuserapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String password;
    private final String user;
    private final String url;
    private Connection connection;

    public Database(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

}
