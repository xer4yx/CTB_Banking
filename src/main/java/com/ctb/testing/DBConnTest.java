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

//        try (Connection connection = DriverManager.getConnection(url, user, password)) {
//
//            String transact = "INSERT INTO users (user_id, fname, mname, lname, username, email, phone_number, password) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//            try (PreparedStatement statement = connection.prepareStatement(transact)) {
//
//                statement.setString(1, "202311051355");
//                statement.setString(2, "Angelo");
//                statement.setString(3, "M");
//                statement.setString(4, "Bicomong");
//                statement.setString(5, "xerayx");
//                statement.setString(6, "xerayx@gmail.com");
//                statement.setString(7, "09288650313");
//                statement.setString(8, "Vertig@6925");
//
//                int affectedRows = statement.executeUpdate();
//                System.out.println("Number of affected rows: " + affectedRows);
//            } catch (SQLException e) {
//                System.err.println("Error while inserting data into users table: " + e.getMessage());
//                e.printStackTrace();
//            }
//        } catch (SQLException e) {
//            System.err.println("Error while connecting to the database: " + e.getMessage());
//            e.printStackTrace();
//        }

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String transact = "INSERT INTO transactions (transaction_id, user_id, transact_type, amount, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(transact)) {

                statement.setString(1, "202311051401");
                statement.setString(2, "202311051355");
                statement.setString(3, "Deposit");
                statement.setString(4,"$500");
                statement.setString(5, "1405110523");

                int affectedRows = statement.executeUpdate();
                System.out.println("Number of affected rows: " + affectedRows);
            } catch (SQLException e) {
                System.err.println("Error while inserting data into users table: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Error while connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
