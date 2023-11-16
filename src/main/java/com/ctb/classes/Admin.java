package com.ctb.classes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.ctb.exceptions.DataDeletionException;
import com.ctb.exceptions.DataRetrievalException;
import com.ctb.exceptions.DataUpdateException;

import java.sql.*;

class Admin extends User {
    private static final Scanner input = new Scanner(System.in);

    /*----------------------Class Methods----------------------*/
    protected static void displayDashboardMenu(final String username) {
        BankSystem.clearConsole();
        System.out.print(
                """
                        ╔═════════════════════════════════════╗
                        ║            Administrator            ║
                        ╚═════════════════════════════════════╝

                        ╔═════════════════════════════════════╗
                        ║         Dashboard Options:          ║
                        ╠═════════════════════════════════════╣
                        ║  1. Manage Users                    ║
                        ║  2. Help  Resources                 ║
                        ║  3. Logout                          ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s""");
    }

    /*----------------------Database Connection----------------------*/
    public void connectToDatabase() {
        try {
            BankSystem.getConnection();
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

        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1:
                displayAllUserData();
                break;
            case 2:
                User.applyProduct();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                updateUser();
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

    public static void handleSettings(String username) {
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
                            ║  7. Make User Admin                 ║
                            ║  8. Make User Customer Service      ║
                            ║  9. Deposit(Savings Only)           ║
                            ║  10. Withdraw(Savings Only)         ║
                            ║  11. Make a Purchase(Credit Only)   ║
                            ║  12. Bills Payment(Credit Only)     ║
                            ║  13. Back to Profile                ║
                            ╚═════════════════════════════════════╝""");
            System.out.print("Enter: ");
            int choice = input.nextInt();
            input.nextLine(); // Move cursor to next line
            switch (choice) {
                case 1:
                    System.out.print("Enter new password: ");
                    String newPassword = input.nextLine();
                    Admin.changePassword(username, newPassword);
                    break;

                case 2:
                    System.out.print("Enter new email: ");
                    String newEmail = input.nextLine();
                    Admin.changeEmail(username, newEmail);
                    break;

                case 3:
                    System.out.print("Enter new phone: ");
                    String newPhoneNumber = input.nextLine();
                    Admin.changePhoneNum(username, newPhoneNumber);
                    break;

                case 4:
                    System.out.print("Enter new username: ");
                    String newUsername = input.nextLine();
                    Admin.changeUsername(username, newUsername);
                    break;

                case 5:
                    System.out.print("Do you want to enable 2FA?(Y/N): ");
                    char new2FA = input.next().charAt(0);
                    input.nextLine(); // Move cursor to next line
                    Admin.change2FAStatus(username, new2FA);
                    break;
                case 6:
                    displayActivityLog(username);
                    break;

                case 7:
                    makeUserAdmin(username);
                    break;
                case 8:
                    makeUserCustomerService(username);
                    break;
                case 9:
                    processDeposit();
                    break;
                case 10:
                    processWithdrawal();
                    break;
                case 11:
                    processPurchase();
                    break;
                case 12:
                    processBills();
                    break;
                case 13:
                    System.out.print("Press Enter to continue...");
                    return;

                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
                    break;
            }
        }

    }

    private static void updateUser() {
        System.out.print(
                """

                        ╭────────────────────────────────────────────────────────────────╮
                        │                         Update User                            │
                        ╰────────────────────────────────────────────────────────────────╯""");
        System.out.print("\nEnter the username of the user you want to update: ");
        String pickedUsername = input.nextLine();
        input.nextLine();
        handleSettings(pickedUsername);
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
                String name = dataSet.getString("username");
                if (name.equals(username)) {
                    System.out.print(
                            "\n──────────────────────────────────────────────────────────────────" +
                                    "\n                          Information:                            " +
                                    "\n──────────────────────────────────────────────────────────────────" +
                                    "\n  User ID                : " + dataSet.getLong("user_id") +
                                    "\n  Name                   : " + dataSet.getString("fname") +
                                    "\n  Username               : " + name +
                                    "\n  Email                  : " + dataSet.getString("email") +
                                    "\n  Phone                  : " + dataSet.getString("phone_number") +
                                    "\n  Two-Factor Enabled     : " + (dataSet.getBoolean("is2fa") ? "Yes" : "No") +
                                    "\n  Is Admin               : " + (dataSet.getBoolean("is_admin") ? "Yes" : "No") +
                                    "\n  Is Customer Service    : "
                                    + (dataSet.getBoolean("is_customerservice") ? "Yes" : "No") +
                                    "\n  Product Type           : " + dataSet.getString("product_type") +
                                    "\n  Balance                : " + dataSet.getDouble("balance") +
                                    "\n──────────────────────────────────────────────────────────────────");
                }
            } else {
                throw new DataRetrievalException("User with username " + username + " not found.");
            }

        } catch (SQLException e) {
            System.err.print("Error in Data Retrieving: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }
    }

    private static void displayAllUserData() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT * FROM users";

            statement = connection.prepareStatement(query);
            dataSet = statement.executeQuery();

            System.out.print(
                    """

                            ╔════════════════════════════════════════════════════════╗
                            ║                   View Users Data                      ║
                            ╚════════════════════════════════════════════════════════╝""");

            while (dataSet.next()) {
                System.out.print(
                        "\n──────────────────────────────────────────────────────────────────" +
                                "\n                          Information:                            " +
                                "\n──────────────────────────────────────────────────────────────────" +
                                "\n  User ID                : " + dataSet.getLong("user_id") +
                                "\n  Name                   : " + dataSet.getString("fname") +
                                "\n  Username               : " + dataSet.getString("username") +
                                "\n  Email                  : " + dataSet.getString("email") +
                                "\n  Phone                  : " + dataSet.getString("phone_number") +
                                "\n  Two-Factor Enabled     : " + (dataSet.getBoolean("is2fa") ? "Yes" : "No") +
                                "\n  Is Admin               : " + (dataSet.getBoolean("is_admin") ? "Yes" : "No") +
                                "\n  Is Customer Service    : "
                                + (dataSet.getBoolean("is_customerservice") ? "Yes" : "No") +
                                "\n  Product Type           : " + dataSet.getString("product_type") +
                                "\n  Balance                : " + dataSet.getDouble("balance") +
                                "\n──────────────────────────────────────────────────────────────────");
            }
        } catch (SQLException e) {
            System.err.print("Error in Data Retrieving: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }
        System.out.print("\nPress enter to continue...");
    }

    private static void makeUserAdmin(String username) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "UPDATE users SET is_admin = ? WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setBoolean(1, true);
            statement.setString(2, username);

            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0) {
                System.out.print("\nUser status updated");
            } else {
                throw new DataUpdateException("Username " + username + "not found.");
            }
        } catch (SQLException e) {
            System.err.print("\nError on Data Update: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement);
        }
    }

    private static void makeUserCustomerService(String username) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "UPDATE users SET is_customerservice = ? WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setBoolean(1, true);
            statement.setString(2, username);

            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0) {
                System.out.print("\nUser status updated");
            } else {
                throw new DataUpdateException("Username " + username + "not found.");
            }
        } catch (SQLException e) {
            System.err.print("\nError on Data Update: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement);
        }
    }

    private static void processDeposit() {
        String username;
        double amount;
        System.out.print("Enter the username of the user to deposit funds: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to deposit: ");
        amount = input.nextInt();
        input.nextLine();
        if (Transaction.depositFunds(username, amount)) {
            System.out.print("Funds deposited successfully.");
        } else {
            System.out.print("Error: Unable to deposit funds.");
        }
    }

    private static void processWithdrawal() {
        String username;
        double amount;
        System.out.print("Enter the username of the user to withdraw funds: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to withdraw: ");
        amount = input.nextInt();
        input.nextLine();
        if (Transaction.withdrawFunds(username, amount)) {
            System.out.print("Funds withdrawn successfully.");
        } else {
            System.out.print("Error: Unable to withdraw funds.");
        }
    }

    private static void processPurchase() {
        String username, purchaseDescription;
        double amount;
        System.out.print("Enter the username of the user to make a purchase: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to withdraw: ");
        amount = input.nextInt();
        input.nextLine();
        System.out.print("Enter the description of the purchase: ");
        purchaseDescription = input.nextLine();
        input.nextLine();
        if (Transaction.makePurchase(username, amount, purchaseDescription)) {
            System.out.print("Purchase made successfully.");
        } else {
            System.out.print("Error: Unable to make purchase.");
        }
    }

    private static void processBills() {
        String username, billDescription;
        double amount;
        System.out.print("Enter the username of the user to pay bills: ");
        username = input.nextLine();
        input.nextLine();
        System.out.print("Enter the amount to withdraw: ");
        amount = input.nextInt();
        input.nextLine();
        System.out.print("Enter the description of the bill: ");
        billDescription = input.nextLine();
        input.nextLine();
        if (Transaction.payBills(username, amount, billDescription)) {
            System.out.print("Bill paid successfully.");
        } else {
            System.out.print("Error: Unable to pay bill.");
        }
    }
}
