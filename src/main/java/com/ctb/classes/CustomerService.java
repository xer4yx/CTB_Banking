package com.ctb.classes;

import com.ctb.exceptions.DataDeletionException;
import com.ctb.exceptions.DataUpdateException;

import java.sql.*;
import java.util.Scanner;

class CustomerService extends User {
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
                        ║  2. Analytics                       ║
                        ║  3. Manage Users                    ║
                        ║  4. Logout                          ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s""");
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
                            ────────────────────────────────────────────""");

            while (dataSet.next()) {
                String feedback = dataSet.getString("feedback");
                if (feedback == null || feedback.isEmpty()) {
                    feedback = "There is no feedback";
                }

                System.out.print(
                        "\nHelp ID: " + dataSet.getLong("hr_id") +
                                "\nType: " + dataSet.getString("hr_type") +
                                "\nDescription: " + dataSet.getString("hr_description") +
                                "\nFeedback: " + feedback +
                                "\n────────────────────────────────────────────");

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

    protected static boolean helpResourceExists(long hr_id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT 1 FROM help_resources WHERE hr_id = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, hr_id);

            resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.print("\nError on Data Retrieval: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement, resultSet);
        }

        return false;
    }

    protected static void replyToHelp() {
        Connection connection = null;
        PreparedStatement statement = null;

        System.out.print("\nEnter the help ID of the help and resources to reply to: ");
        long helpID = input.nextLong();
        input.nextLine(); // Consume the newline character

        if (!helpResourceExists(helpID)) {
            System.out.println("Help ID not found");
            return;
        }
        System.out.print("Enter your feedback: ");
        String feedback = input.nextLine();

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "UPDATE help_resources SET feedback = ? WHERE hr_id = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, feedback);
            statement.setLong(2, helpID);

            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0) {
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

    public static void displayAnalytics(String username) {
        try {
            Connection conn = BankSystem.getConnection();

            // Display total number of help resources
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM help_resources");
            if (rs.next()) {
                System.out.println("Total number of help resources: " + rs.getInt(1));
            }

            // Display number of users with 2FA enabled
            rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE is2fa = 1");
            if (rs.next()) {
                System.out.println("Number of users with 2FA enabled: " + rs.getInt(1));
            }

            // Display number of active sessions
            rs = stmt.executeQuery("SELECT COUNT(*) FROM sessions");
            if (rs.next()) {
                System.out.println("Number of active sessions: " + rs.getInt(1));
            }

            // Prompt user to press enter to continue
            System.out.println("\n\nPress Enter to continue...");

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void handleManageUsers() {
        System.out.print(
                """

                        ╔═══════════════════════════════╗
                        ║          Manage Users         ║
                        ╠═══════════════════════════════╣
                        ║  1. View Users Data           ║
                        ║  2. Add Users                 ║
                        ║  3. Delete Users              ║
                        ║  4. Update Users              ║
                        ║  5. Exit                      ║
                        ╚═══════════════════════════════╝""");

        System.out.print("\nEnter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1:
                System.out.print("\nEnter the username of the user: ");
                String username = input.nextLine();
                displayUserData(username);
                break;
            case 2:
                User.applyProduct();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                System.out.print("\nEnter the username of the user: ");
                String userToUpdate = input.nextLine();
                handleSettings(userToUpdate);
                break;
            case 5:
                return;
            default:
                System.out.print("Invalid choice. Please try again.");
                break;
        }
    }

    private static void deleteUserByUsername(String userToDelete) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "DELETE FROM users WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, userToDelete);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.print("\nUser data safely deleted");
            } else {
                throw new DataDeletionException("\nUsername " + userToDelete + "is not found.");
            }

        } catch (SQLException e) {
            System.err.print("\nError on Data Deletion: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement);
        }
    }

    private static void deleteUser() {
        System.out.print(
                """

                        ╭────────────────────────────────────────────────────────────────╮
                        │                         Delete User                            │
                        ╰────────────────────────────────────────────────────────────────╯""");
        System.out.print("\nEnter the username of the user to delete: ");
        String user = input.nextLine();
        input.nextLine();
        deleteUserByUsername(user);
    }

    private static void displayUserData(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT * FROM users WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            dataSet = statement.executeQuery();
            if (dataSet.next()) {
                System.out.println("User ID: " + dataSet.getLong("user_id"));
                System.out.println("First Name: " + dataSet.getString("fname"));
                System.out.println("Middle Name: " + dataSet.getString("mname"));
                System.out.println("Last Name: " + dataSet.getString("lname"));
                System.out.println("Username: " + dataSet.getString("username"));
                System.out.println("Email: " + dataSet.getString("email"));
                System.out.println("Phone Number: " + dataSet.getString("phone_number"));
                System.out.println("2FA Enabled: " + (dataSet.getInt("is2fa") == 1 ? "Yes" : "No"));
                System.out.println("Is Admin: " + (dataSet.getInt("is_admin") == 1 ? "Yes" : "No"));
                System.out
                        .println("Is Customer Service: " + (dataSet.getInt("is_customerservice") == 1 ? "Yes" : "No"));
                System.out.println("Product Type: " + dataSet.getString("product_type"));
                System.out.println("Balance: " + dataSet.getDouble("balance"));
            } else {
                System.out.println("No user found with username: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }
    }

    public static boolean isUserExists(String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void handleSettings(String username) {
        if (!CustomerService.isUserExists(username)) {
            CustomerService.handleManageUsers();
        }
        while (true) {
            displayUserData(username);
            System.out.print(
                    """
                            ╔═════════════════════════════════════╗
                            ║           Manage Account            ║
                            ╠═════════════════════════════════════╣
                            ║  1. Change Password                 ║
                            ║  2. Change Email                    ║
                            ║  3. Change Phone                    ║
                            ║  4. Change Username                 ║
                            ║  5. Enable/Disable 2FA              ║
                            ║  6. Show Activity Log               ║
                            ║  7. Back to Profile                 ║
                            ╚═════════════════════════════════════╝""");
            System.out.print("Enter: ");
            int choice = input.nextInt();
            input.nextLine(); // Move cursor to next line
            switch (choice) {
                case 1:
                    System.out.print("Enter new password: ");
                    String newPassword = input.nextLine();
                    CustomerService.changePassword(username, newPassword);
                    break;
                case 2:
                    System.out.print("Enter new email: ");
                    String newEmail = input.nextLine();
                    CustomerService.changeEmail(username, newEmail);
                    break;
                case 3:
                    System.out.print("Enter new phone: ");
                    String newPhoneNumber = input.nextLine();
                    CustomerService.changePhoneNum(username, newPhoneNumber);
                    break;
                case 4:
                    System.out.print("Enter new username: ");
                    String newUsername = input.nextLine();
                    CustomerService.changeUsername(username, newUsername);
                    break;
                case 5:
                    System.out.print("Do you want to enable 2FA?(Y/N): ");
                    char new2FA = input.next().charAt(0);
                    input.nextLine(); // Move cursor to next line
                    CustomerService.change2FAStatus(username, new2FA);
                    break;
                case 6:
                    displayActivityLog(username);
                    break;
                case 7:
                    System.out.print("Press Enter to continue...");
                    return;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

}
