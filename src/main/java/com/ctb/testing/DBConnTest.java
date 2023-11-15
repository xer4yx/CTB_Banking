package com.ctb.testing;

import java.sql.*;

public class DBConnTest {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/ctb_banking";
        String username = "root";
        String password = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            if (connection != null) {
                System.out.println("Connected to the database!");

                // Define the SQL SELECT statements
                String[] selectStatements = {
                        "SELECT * FROM users",
                        "SELECT * FROM transactions",
                        "SELECT * FROM sessions",
                        "SELECT * FROM help_resources"
                };

                // Create a Statement object
                Statement statement = connection.createStatement();

                // Execute each SELECT statement and print the results
                for (String selectStatement : selectStatements) {
                    ResultSet resultSet = statement.executeQuery(selectStatement);
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    System.out.println("\nTable: " + metaData.getTableName(1));

                    // Print column names
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(metaData.getColumnName(i) + "\t");
                    }
                    System.out.println();

                    // Print row data
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(resultSet.getString(i) + "\t");
                        }
                        System.out.println();
                    }
                }

                connection.close();
            } else {
                System.out.println("Failed to connect to the database!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}