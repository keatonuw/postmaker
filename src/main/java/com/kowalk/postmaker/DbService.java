package com.kowalk.postmaker;

import java.sql.*;
import java.util.Properties;

public class DbService {

    private final Connection connection;

    public DbService(String username, String password, String url) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);

        this.connection = DriverManager.getConnection(url, props);
    }

    public void getAll() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM articles");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        rs.close();
        stmt.close();
    }
    
}
