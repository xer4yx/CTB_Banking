package com.ctb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import static com.ctb.classes.BankSystem.*;

public class Display {
    private static final Scanner input = new Scanner(System.in);

    public static void displayMainMenu() {
        System.out.print(
                """

                        ╔══════════════════════════════════════╗
                        ║      ╔═══════════════════════╗       ║
                        ║      ║   CENTRAL TRUST BANK  ║       ║
                        ║      ╚═══════════════════════╝       ║
                        ╚══════════════════════════════════════╝
                        ┌──────────────────────────────────────┐
                        │ ┌──────────────────────────────────┐ │
                        │ │  1. Login                        │ │
                        │ │  2. Product Application          │ │
                        │ │  3. Forgot Password              │ │
                        │ │  4. Exit                         │ │
                        │ └──────────────────────────────────┘ │
                        └──────────────────────────────────────┘
                        Enter your choice:\s""");
    }

    public static boolean loginUser() {
        BankSystem.clearConsole(); // TODO: delete this
        System.out.print(
                """

                        ╔══════════════════════════════════════╗
                        ║      ╔═══════════════════════╗       ║
                        ║      ║   CENTRAL TRUST BANK  ║       ║
                        ║      ╚═══════════════════════╝       ║
                        ╚══════════════════════════════════════╝

                        ╔══════════════════════════════════════╗
                        ║                Login                 ║
                        ╚══════════════════════════════════════╝""");
        String username, password;
        System.out.print("\nEnter username: ");
        username = input.nextLine();
        System.out.print("Enter password: ");
        password = input.nextLine();

        if (SecuritySystem.authenticateUser(username, password)) {
            System.out.print(
                    """

                            ╔══════════════════════════════════════╗
                            ║          Login successful!           ║
                            ╚══════════════════════════════════════╝""");
            System.out.print("\nPress Enter to continue...");
            return true;
        } else {
            System.out.print(
                    """
                            Please try again.

                            """);
            System.out.print("Press Enter to continue...");
            input.nextLine();
            BankSystem.clearConsole(); // TODO: delete this
            return false;
        }
    }

    protected static void logout(String username) {
        System.out.print("\nLogging out...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Session.saveSession(BankSystem.getCurrentLoggedInUser(), "Logout");
        setCurrentLoggedInUser(null);
        System.out.print("\nLogged out successfully.");
    }

    public static void handleDashboardOptions() {
        while (true) {
            User.displayDashboardMenu(getCurrentLoggedInUser());
            int choice = input.nextInt();
            input.nextLine();

            try {
                Connection conn = BankSystem.getConnection();
                String sql = "SELECT * FROM users WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, getCurrentLoggedInUser());
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    if (rs.getBoolean("is_admin")) {
                        switch (choice) {
                            case 1:
                                Admin.handleManageUsers();
                                break;
                            case 2:
                                CustomerService.displayAllHR();
                                CustomerService.replyToHelp();
                                break;
                            case 3:
                                logout(getCurrentLoggedInUser());
                                System.out.print("\nPress Enter to continue...");
                                input.nextLine();
                                BankSystem.clearConsole();
                                return;
                            default:
                                System.out.print("*Invalid choice. Please select a valid option.");
                        }
                    } else if (rs.getBoolean("is_customerservice")) {
                        switch (choice) {
                            case 1:
                                CustomerService.displayAllHR();
                                CustomerService.replyToHelp();
                                break;
                            case 2:
                                logout(getCurrentLoggedInUser());
                                setCurrentLoggedInUser("");
                                System.out.print("Press Enter to continue...");
                                input.nextLine();
                                BankSystem.clearConsole();
                                return;
                            default:
                                System.out.print("*Invalid choice. Please select a valid option.");
                        }
                    } else {
                        switch (choice) {
                            case 1:
                                handleProductOptions(getCurrentProductType(getCurrentLoggedInUser()),
                                        getCurrentLoggedInUser());
                                break;
                            case 2:
                                User.displayProfile();
                                break;
                            case 3:
                                displayAnalytics(getCurrentLoggedInUser());
                                break;
                            case 4:
                                handleHelpAndResources(getCurrentLoggedInUser());
                                break;
                            case 5:
                                logout(getCurrentLoggedInUser());
                                System.out.print(
                                        """
                                                ──────────────────────────────────
                                                Press Enter to continue...""");
                                input.nextLine();
                                BankSystem.clearConsole();
                                return;
                            default:
                                System.out.print("*Invalid choice. Please select a valid option.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayAnalytics(String currentLoggedInUser) {
    }

    public static void handleProductOptions(final String producttype, final String username) {
        if (Objects.equals(producttype, "Savings Account")) {
            displaySavingsMenu(username);
        } else if (Objects.equals(producttype, "Credit Account")) {
            displayCreditMenu(username);
        }
    }

    protected static void displaySavingsMenu(final String username) {
        handleTransactionCenter(username);
    }

    protected static void displayCreditMenu(final String username) {
        handleCreditCenter(username);
    }

    protected static void displayTransactionMenu(final String username) { // TODO: delete parameter
        System.out.print(
                """

                        ╔═════════════════════════════════════╗
                        ║         Transaction Center:         ║
                        ╠═════════════════════════════════════╣
                        ║  1. Deposit Funds                   ║
                        ║  2. Withdraw Funds                  ║
                        ║  3. View Transaction History        ║
                        ║  4. Back to Dashboard               ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s""");
    }

    protected static void displayTransactionCredit(final String username) { // TODO: delete parameter
        System.out.print(
                """
                        ╔═════════════════════════════════════╗
                        ║         Transaction Center:         ║
                        ╠═════════════════════════════════════╣
                        ║  1. Make a Purchase                 ║
                        ║  2. Pay Bills                       ║
                        ║  3. View Transaction History        ║
                        ║  4. Back to Dashboard               ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s""");
    }

    protected static void handleTransactionCenter(final String username) {
        while (true) {
            displayTransactionMenu(username);
            int transactionChoice = input.nextInt();
            input.nextLine();

            switch (transactionChoice) {
                case 1:
                    User.processDeposit(username);
                    break;
                case 2:
                    User.processWithdrawal(username);
                    break;
                case 3:
                    ;
                    BankSystem.clearConsole(); // TODO: delete this
                    displayTransaction(username);
                    System.out.print("Press Enter to continue...");
                    input.nextLine();
                    break;
                case 4:
                    return;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
            }
        }
    }

    protected static void handleCreditCenter(final String username) {
        while (true) {
            displayTransactionCredit(username);

            int transactionChoice = input.nextInt();
            input.nextLine();

            switch (transactionChoice) {
                case 1:
                    User.processPurchase(username);
                    BankSystem.clearConsole(); // TODO: delete this
                    break;
                case 2:
                    User.processBills(username);
                    BankSystem.clearConsole(); // TODO: delete this
                    break;
                case 3:
                    BankSystem.clearConsole(); // TODO: delete this
                    displayTransaction(username);
                    System.out.print("Press Enter to continue...");
                    input.nextLine();
                    break;
                case 4:
                    return;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void displayTransaction(String username) {
    }

    public static void handleHelpAndResources(final String username) { // TODO: delete parameter
        BankSystem.clearConsole(); // TODO: delete this
        System.out.print(
                """
                        ╔═════════════════════════════════════╗
                        ║          Help  Resources            ║
                        ╠═════════════════════════════════════╣
                        ║  1. Chat with AI Assistant          ║
                        ║  2. Contact US                      ║
                        ║  3. Back to Dashboard               ║
                        ╚═════════════════════════════════════╝
                        Enter your choice:\s""");

        int jhchoice = input.nextInt();
        input.nextLine();
        switch (jhchoice) { // TODO: modularize
            case 1:
                System.out.print(
                        """
                                Hi! I'm your AI Assistant. How may I help you?
                                Enter inquiry:\\s""");
                String message = input.nextLine();
                HelpAndResources.chatBot(message, BankSystem.getCurrentLoggedInUser());
                System.out.print("Press Enter to continue...");
                input.nextLine();
                break;
            case 2:
                BankSystem.clearConsole(); // TODO: delete this
                System.out.print(
                        """
                                ╭────────────────────────────────────────────────╮
                                │                  Contact Us                    │
                                ├────────────────────────────────────────────────┤
                                │  Email: Uniportal@proton.me                    │
                                │  Phone: 1-800-123-4567                         │
                                │  Address: 123 Main St, New York, NY 10001      │
                                ╰────────────────────────────────────────────────╯
                                ╭────────────────────────────────────────────────╮
                                │  1. Send a message                             │
                                │  2. Back to Dashboard                          │
                                ╰────────────────────────────────────────────────╯
                                Enter your choice:\s""");

                int schoice = input.nextInt();
                if (schoice == 1) {
                    User.askHelp(getCurrentLoggedInUser());
                    System.out.print(
                            """
                                    Message sent successfully!
                                    Press Enter to continue...
                                    """);
                    input.nextLine();
                } else if (schoice == 2) {
                    break;
                } else {
                    System.out.print("*Invalid choice. Please select a valid option.");
                    return;
                }
                break;
            case 3:
                return;
            default:
                System.out.print("Press Enter to continue...");
                input.nextLine();
        }
    }

    public static void handleProductApplication() {
        User.applyProduct();
    }

}
