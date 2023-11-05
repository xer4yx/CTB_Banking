package com.ctb.testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ctb_banking";
        String user = "root";
        String password = "Vertig@6925";
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            String transact = "INSERT INTO users (user_id, fname, mname, lname, username, email, phone_number, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(transact)) {

                statement.setString(1, "12345678901");
                statement.setString(2, "John");
                statement.setString(3, "M");
                statement.setString(4, "Doe");
                statement.setString(5, "john.doe");
                statement.setString(6, "john.doe@example.com");
                statement.setString(7, "1234567890");
                statement.setString(8, "hashed_password");

                int affectedRows = statement.executeUpdate();
                System.out.println("Number of affected rows: " + affectedRows);
            } catch (SQLException e) {
                System.out.println("Error while inserting data into users table: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
