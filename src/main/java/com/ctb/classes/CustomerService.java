package com.ctb.classes;

import com.ctb.exceptions.DataRetrievalException;
import com.ctb.exceptions.DataUpdateException;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

//TODO: Add more codes and functionalities to customer service
class CustomerService extends User{
    private static final Scanner input = new Scanner(System.in);
    protected static void displayDashboardMenu() {
        System.out.print(
                """
                        ╔═════════════════════════════════════╗
                        ║          Customer Service           ║
                        ╚═════════════════════════════════════╝
                                                               
                        ╔═════════════════════════════════════╗
                        ║         Dashboard Options:          ║
                        ╠═════════════════════════════════════╣
                        ║  1. Messages                        ║
                        ║  2. Logout                          ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s"""
        );
    }
    
    protected static void displayHelpHistory(long userID) { //TODO: Transfer to User
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;
        boolean helpFound = false;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT * FROM help_resources WHERE user_id = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, userID);

            dataSet = statement.executeQuery();

            if (dataSet.next()) {
                long user_id = dataSet.getLong("user_id");
                if(Objects.equals(user_id, userID)) {
                    System.out.print(
                            """
    
                                    ╔═════════════════════════════════════════════════════════════╗
                                    ║                        Help History                         ║
                                    ╚═════════════════════════════════════════════════════════════╝
                                    ───────────────────────────────────────────────────────────────"""
                    );
                    System.out.print(
                            "\nHelp ID: " + dataSet.getLong("hr_id") +
                            "\nType: " + dataSet.getString("hr_type") +
                            "\nDescription: " + dataSet.getString("hr_description")
                    );
                    if (!Objects.equals(dataSet.getString("feedback"), null)) {
                        System.out.print(
                                "\nFeedback: " + dataSet.getString("feedback") +
                                "\n───────────────────────────────────────────────────────────────"
                        );

                    } else {
                        System.out.print(
                                """
                                        Feedback: No feedback yet.
                                        ───────────────────────────────────────────────────────────────"""
                        );

                    }
                    helpFound = true;
                }
            } else {
                throw new DataRetrievalException("User ID does not exist");
            }
        } catch (SQLException e) {
            System.out.print("\nError on Data Retrieval: " + e.getMessage());
        } finally {
            if (!helpFound) {
                System.out.print("No help history available for the user.");
            }

            BankSystem.closeResources(connection, statement, dataSet);
        }
    }

    protected static void displayAllHR() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;
        boolean helpFound = false;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT * FROM help_resources";

            statement = connection.prepareStatement(query);
            dataSet = statement.executeQuery();

            System.out.print(
                    """
    
                            ╭───────────────────────────────────────────╮
                            │             Help & Resources              │
                            ╰───────────────────────────────────────────╯
                            ────────────────────────────────────────────"""
            );

            while (dataSet.next()) {
                System.out.print(
                        "\nHelp ID: " + dataSet.getLong("hr_id") +
                        "\nType: " + dataSet.getString("hr_type") +
                        "\nDescription: " + dataSet.getString("hr_description") +
                        "\nFeedback: " + dataSet.getString("feedback") +
                        "────────────────────────────────────────────"
                );

                helpFound = true;
            }

        } catch (SQLException e) {
            System.out.print("\nError on Data Retrieval: " + e.getMessage());
        } finally {
            if (!helpFound) {
                System.out.print("\nNo Help is available.");
            }

            BankSystem.closeResources(connection, statement, dataSet);
        }
    }

    protected static void replyToHelp() {
        Connection connection = null;
        PreparedStatement statement = null;

        System.out.print("\nEnter the help ID of the help and resources to reply to: ");
        long helpID = input.nextLong();
        
        System.out.print("Enter your feedback: ");
        String feedback = input.nextLine();

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "UPDATE help_resources SET feedback = ? WHERE hr_id = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, feedback);
            statement.setLong(2, helpID);

            int updatedRows = statement.executeUpdate();
            if(updatedRows > 0) {
                System.out.print("\nFeedback has been sent.");
            } else {
                throw new DataUpdateException("Help ID not found");
            }
        } catch (SQLException e) {
            System.err.print("\nError on Data Update: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement);
        }
    }
}
