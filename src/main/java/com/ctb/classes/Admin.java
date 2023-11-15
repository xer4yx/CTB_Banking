package com.ctb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

class Admin extends User {
    private String adminID;
    private static final Scanner input = new Scanner(System.in);
    private static Connection conn;

    /*----------------------Setter Methods----------------------*/
    private void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    /*----------------------Getter Methods----------------------*/
    private String getAdminID() {
        return adminID;
    }

    /*----------------------Class Methods----------------------*/
    protected static void displayDashboardMenu(final String username) {
        System.out.print("\033[H\033[2J");
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
            Admin.conn = BankSystem.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteUserByUsername(String userToDelete) {
        try {
            Connection conn = BankSystem.getConnection();
            conn.setAutoCommit(false); // start transaction

            // delete from transactions
            String sql = "DELETE FROM transactions WHERE user_id IN (SELECT user_id FROM users WHERE username = ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userToDelete);
            pstmt.executeUpdate();

            // delete from sessions
            sql = "DELETE FROM sessions WHERE user_id IN (SELECT user_id FROM users WHERE username = ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userToDelete);
            pstmt.executeUpdate();

            // delete from help_resources
            sql = "DELETE FROM help_resources WHERE user_id IN (SELECT user_id FROM users WHERE username = ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userToDelete);
            pstmt.executeUpdate();

            // delete from users
            sql = "DELETE FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userToDelete);
            int affectedRows = pstmt.executeUpdate();

            conn.commit(); // end transaction

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    protected static void handleManageUsers(String username) { // FIXME: Remove unused/obsolete parameter
        System.out.print("\033[H\033[2J");
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

    private static void updateUser() {
        System.out.print("\033[H\033[2J");
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

    private static void deleteUser() {
        System.out.print("\033[H\033[2J");
        System.out.print(
                """

                        ╭────────────────────────────────────────────────────────────────╮
                        │                         Delete User                            │
                        ╰────────────────────────────────────────────────────────────────╯""");
        System.out.print("\nEnter the username of the user to delete: ");
        String user = input.nextLine();
        input.nextLine();
        if (deleteUserByUsername(user)) {
            System.out.printf("User with username '%s' has been deleted.", user);
        } else {
            System.out.printf("User with username '%s' not found.", user);
        }
    }

    private static void displayUserData(String username) {
        for (final User user : BankSystem.users) {
            if (Objects.equals(getUsername(), username)) {
                handleUserData(user);
                return; // Exit the loop once the user is found and displayed
            }
        }
        System.out.printf("User with username '%s' not found.", username);
    }

    private static void handleUserData(User user) {
        // TODO: Separate method for displaying user info
        // CONVERT: List -> Database
        System.out.print(
                "\n──────────────────────────────────────────────────────────────────" +
                        "\n                          Information:                            " +
                        "\n──────────────────────────────────────────────────────────────────" +
                        "\n  User ID                : " + user.getUserID() +
                        "\n  Name                   : " + user.getName() +
                        "\n  Username               : " + getUsername() +
                        "\n  Is Admin               : " + (BankSystem.isAdmin(getUsername()) ? "Yes" : "No") +
                        "\n  Is Customer Service    : " + (BankSystem.isCustomerService(getUsername()) ? "Yes" : "No") +
                        "\n  Product Type           : " + user.getProductType() +
                        "\n  Balance                : " + user.getBalance());

        System.out.print(
                """

                        ──────────────────────────────────────────────────────────────────
                                                   Profiles:                             \s
                        ──────────────────────────────────────────────────────────────────""");
        for (final Profile profile : user.userProfile) {
            System.out.print(
                    "\n Email                  : " + profile.getEmail() +
                            "\n  Phone                  : " + profile.getPhoneNumber() +
                            "\n  Two-Factor Enabled     : " + (profile.get2FAStatus() ? "Yes" : "No"));
        }

        System.out.print(
                """

                        ──────────────────────────────────────────────────────────────────
                                              Transaction History:
                        ──────────────────────────────────────────────────────────────────""");
        for (final Transaction transaction : user.userTransaction) {
            System.out.print(
                    "\n  Transaction ID         : " + transaction.getTransactionID() +
                            "\n  Transaction Type       : " + transaction.getTransactionType() +
                            "\n  Amount                 : " + transaction.getAmount() +
                            "\n  Timestamp              : " + transaction.getTimeStamp());
        }

        System.out.print(
                """

                        ──────────────────────────────────────────────────────────────────
                                                  Sessions:
                        ──────────────────────────────────────────────────────────────────""");

        for (final Session session : user.userSessions) {
            System.out.print(
                    "\n  Session ID             : " + session.getSessionID() +
                            "\n  Username               : " + getUsername() +
                            "\n  Timestamp              : " + session.getTimeStamp());
        }

        System.out.print(
                """

                        ──────────────────────────────────────────────────────────────────
                                             Product Applications:
                        ──────────────────────────────────────────────────────────────────""");
        for (final ProductApplication productApp : user.userProductApplications) {
            System.out.print(
                    "\n  Product ID             : " + productApp.getProductID() +
                            "\n  Product Type           : " + productApp.getProductType());

        }

        System.out.print(
                """

                        ──────────────────────────────────────────────────────────────────
                                              Help and Resources:
                        ──────────────────────────────────────────────────────────────────""");
        for (final HelpAndResources resources : user.userHelpAndResources) {
            System.out.print(
                    "\n  Help ID                : " + resources.getHelpID() +
                            "\n  Type                   : " + resources.getH_rType() +
                            "\n  Description            : " + resources.getH_rDescription() +
                            "\n  Feedback               : " + resources.getFeedback() +
                            "\n──────────────────────────────────────────────────────────────────");
        }
        System.out.print(
                """

                        ╔═══════════════════════════════════════════════════════════════════════════╗
                        ║╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳╳║
                        ╚═══════════════════════════════════════════════════════════════════════════╝""");
    }

    private static void displayAllUserData() {
        // TODO: Separate method for displaying user info
        // CONVERT: List -> Database
        System.out.print("\033[H\033[2J");
        System.out.print(
                """

                        ╔════════════════════════════════════════════════════════╗
                        ║                   View Users Data                      ║
                        ╚════════════════════════════════════════════════════════╝""");
        for (final User user : BankSystem.users) {
            handleUserData(user);
        }
        System.out.print("\nPress enter to continue...");
    }

    private static void makeUserAdmin(String username) { // CONVERT: List -> Database
        for (final User user : BankSystem.users) {
            if (getUsername().equals(username)) {
                user.setAdminStatus(true);
                System.out.printf("User %s is now an admin.", getUsername());
                BankSystem.saveDataToFile();
            }
        }
    }

    private static void makeUserCustomerService(String username) { // CONVERT: List -> Database
        for (final User user : BankSystem.users) {
            if (getUsername().equals(username)) {
                user.setCSStatus(true);
                System.out.printf("User %s is now a customer service.", getUsername());
                BankSystem.saveDataToFile();
            }
        }
    }

    public static void handleSettings(String username) {
        while (true) {
            // FIXME: Remove unused vars
            String newPassword, newEmail, newPhoneNumber, newUsername;
            char new2FA;
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
            input.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter new password: ");
                    newPassword = input.nextLine();
                    Admin.changePassword(username);
                    break;

                case 2:
                    System.out.print("Enter new email: ");
                    newEmail = input.nextLine();
                    Admin.changeEmail(username);
                    break;

                case 3:
                    System.out.print("Enter new phone: ");
                    newPhoneNumber = input.nextLine();
                    Admin.changePhoneNum(username);
                    break;

                case 4:
                    System.out.print("Enter new username: ");
                    newUsername = input.nextLine();
                    Admin.changeUsername(username);
                    break;

                case 5:
                    System.out.print("Do you want to enable 2FA?(Y/N): ");
                    new2FA = input.next().charAt(0);
                    Admin.change2FAStatus(username);
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
            System.out.print("\033[H\033[2J");
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
