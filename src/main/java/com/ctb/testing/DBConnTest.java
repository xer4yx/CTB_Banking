package com.ctb.testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnTest {
    public static void main(String[] args) {
        // JDBC URL for MySQL (assuming your database is running locally)
        String jdbcURL = "jdbc:mysql://localhost:3306/bank_system";
        String username = "root";
        String password = "password";

        try {
            // Registering the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Creating a connection to your database
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            if (connection != null) {
                System.out.println("Connected to the database!");
                // You can perform further operations here if needed
            } else {
                System.out.println("Failed to connect to the database!");
            }

            // Close the connection when done
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
