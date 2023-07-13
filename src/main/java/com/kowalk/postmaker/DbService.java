package com.kowalk.postmaker;

import java.sql.*;
import java.util.Properties;

public class DbService {
    private final Properties props;
    private final String url;

    public DbService(String username, String password, String url) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        this.props = props;
        this.url = url;
    }

    public void getAll() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, props)) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM articles");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPost(PostModel model) {
        String query = "INSERT INTO articles(title, subtitle, summary, contents, posted) VALUES(?, ?, ?, ?, ?)";

        try (
            Connection connection = DriverManager.getConnection(url, props);
            PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            // set statement values
            pstmt.setString(1, model.getTitle());
            pstmt.setString(2, model.getSubtitle());
            pstmt.setString(3, model.getSummary());
            pstmt.setString(4, model.getContents());
            pstmt.setDate(5, model.getPosted());

            // do update
            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
