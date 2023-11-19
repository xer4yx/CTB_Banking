package com.ctb.classes;

import com.ctb.exceptions.DataRetrievalException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static com.ctb.classes.BankSystem.*;

public class User {
    private static final Scanner input = new Scanner(System.in);
    private static final Calendar calendar = Calendar.getInstance();
    private static final Date date = calendar.getTime();
    private static boolean isAdmin;
    private String name;
    private static String username;
    private String password;
    private String productType;
    private static boolean isCustomerService;
    private double balance;
    public List<Transaction> userTransaction = new LinkedList<>();
    public List<Session> userSessions = new LinkedList<>();
    public List<HelpAndResources> userHelpAndResources = new LinkedList<>();

    /*----------------------Constructor Methods----------------------*/

    /*----------------------Setter Methods----------------------*/
    public void setUserID(String userID) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        User.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setCSStatus(boolean isCustomerService) {
        User.isCustomerService = isCustomerService;
    }

    public void setAdminStatus(boolean isAdmin) {
        User.isAdmin = isAdmin;
    }

    public void setBalance(double balance) {
        this.balance += balance;
    }

    /*----------------------Getter Methods----------------------*/
    public static long getUserId(String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                return dataSet.getLong("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getName() {
        return this.name;
    }

    public static String getUsername() {
        return username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getProductType() {
        return this.productType;
    }

    public double getBalance() {
        return balance;
    }

    /*----------------------Class Methods----------------------*/
    protected static void displayDashboardMenu(final String username) {
        if (Objects.equals(BankSystem.getCurrentLoggedInUser(), username)) {
            try {
                Connection conn = BankSystem.getConnection();
                String sql = "SELECT * FROM users WHERE username = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, username);
                ResultSet dataSet = statement.executeQuery();
                if (dataSet.next()) {
                    if (dataSet.getBoolean("is_admin")) {
                        Admin.displayDashboardMenu(username);
                    } else if (dataSet.getBoolean("is_customerservice")) {
                        CustomerService.displayDashboardMenu();
                    } else {
                        System.out.print(
                                """

                                        ╭─────────────────────────────────────╮
                                        │         CENTRAL TRUST BANK          │
                                        ╰─────────────────────────────────────╯
                                        """);
                        System.out.print("Welcome " + BankSystem.getCurrentLoggedInUser() + "!");
                        System.out.print("\nCurrent Balance: $"
                                + BankSystem.getCurrentBalance(BankSystem.getCurrentLoggedInUser()));
                        System.out.print(
                                """

                                        ╔═════════════════════════════════════╗
                                        ║         Dashboard Options:          ║
                                        ╠═════════════════════════════════════╣
                                        ║  1. Transaction Center              ║
                                        ║  2. User Profile                    ║
                                        ║  3. Data Analytics Dashboard        ║
                                        ║  4. Help  Resources                 ║
                                        ║  5. Logout                          ║
                                        ╚═════════════════════════════════════╝
                                        Enter your choice:\s""");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void displayProfile() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT fname, mname, lname, username, email, phone_number, product_type, is2fa, balance FROM users WHERE username = ?"; // Added
                                                                                                                                                    // balance

            statement = connection.prepareStatement(query);
            statement.setString(1, BankSystem.getCurrentLoggedInUser());

            dataSet = statement.executeQuery();
            if (dataSet.next()) {
                String user = dataSet.getString("username");
                if (Objects.equals(user, BankSystem.getCurrentLoggedInUser())) {
                    String show2FAStatus = dataSet.getBoolean("is2fa") ? "Enabled" : "Disabled";
                    System.out.print(
                            "\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" +
                                    "\n                      User Profile                            " +
                                    "\n══════════════════════════════════════════════════════════════" +
                                    "\n  Name: " + dataSet.getString("fname") +
                                    "\n  Username: " + user +
                                    "\n  Email: " + dataSet.getString("email") +
                                    "\n  Phone: " + dataSet.getString("phone_number") +
                                    "\n  Account: " + dataSet.getString("product_type") +
                                    "\n  Balance: " + dataSet.getDouble("balance") + // Fetch balance
                                    "\n  Two Factor Authentication: " + show2FAStatus +
                                    "\n══════════════════════════════════════════════════════════════");
                    displayUserSettings(User.getUsername());
                }
            } else {
                throw new DataRetrievalException("");
            }
        } catch (SQLException e) {
            System.err.print("\nError on Data Update: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }
    }

    public static void displayUserSettings(String username) {
        while (true) {
            System.out.print(
                    """

                            ╭────────────────────────╮
                            │     User Settings      │
                            ├────────────────────────┤
                            │ 1. Manage Account      │
                            │ 2. Back to Dashboard   │
                            ╰────────────────────────╯
                            """);
            System.out.print("Enter: ");
            int settingChoice = input.nextInt();
            switch (settingChoice) {
                case 1:
                    handleSettings();
                    break;
                case 2:
                    return;
                default:
                    System.out.print("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    public static void displayActivityLog(String username) {
        System.out.print(
                """

                        ╭───────────────────────────╮
                        │      Activity Log         │
                        ├───────────────────────────┤
                        │ 1. Transaction History    │
                        │ 2. Session History        │
                        │ 3. Help History           │
                        ╰───────────────────────────╯""");

        System.out.print("\nEnter: ");
        int choice = input.nextInt();

        switch (choice) {
            case 1:
                displayTransaction(username);
                break;
            case 2:
                displaySessions();
                break;
            case 3:
                CustomerService.displayHelpHistory(BankSystem.getCurrentUserID());
                break;
            default:
                System.out.print("*Invalid choice. Please select a valid option.");
                break;
        }
    }

    public static void displaySessions() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT * FROM sessions WHERE user_id = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, BankSystem.getCurrentUserID());

            dataSet = statement.executeQuery();
            boolean hasSessions = false;
            while (dataSet.next()) {
                long user_id = dataSet.getLong("user_id");
                if (Objects.equals(user_id, getCurrentUserID())) {
                    if (!hasSessions) {
                        System.out.print(
                                "\n╔════════════════════════════════════════════╗" +
                                        "\n║               Session History              ║" +
                                        "\n╚════════════════════════════════════════════╝" +
                                        "\n User: " + getCurrentLoggedInUser() +
                                        "\n──────────────────────────────────────────────");
                        hasSessions = true;
                    }
                    System.out.print(
                            "\n Session ID: " + dataSet.getString("session_id") +
                                    "\n Timestamp: " + dataSet.getDate("timestamp") +
                                    "\n──────────────────────────────────────────────");
                }
            }
            if (!hasSessions) {
                throw new DataRetrievalException("There are no sessions for this user.");
            }

        } catch (SQLException e) {
            System.err.print("Error in Data Retrieving: " + e.getMessage());
        } finally {
            closeResources(connection, statement, dataSet);
        }
    }

    public static void handleSettings() {

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
        System.out.print("\nEnter: ");
        int accChoice = input.nextInt();

        switch (accChoice) {
            case 1:
                input.nextLine(); // Move cursor to next line
                System.out.print("\nEnter new password: ");
                String newPassword = input.nextLine();
                changePassword(BankSystem.getCurrentLoggedInUser(), newPassword);
                break;

            case 2:
                input.nextLine(); // Move cursor to next line
                System.out.print("\nEnter new email: ");
                String newEmail = input.nextLine();
                changeEmail(BankSystem.getCurrentLoggedInUser(), newEmail);
                break;

            case 3:
                input.nextLine(); // Move cursor to next line
                System.out.print("\nEnter new phone: ");
                String newPhoneNumber = input.nextLine();
                changePhoneNum(BankSystem.getCurrentLoggedInUser(), newPhoneNumber);
                break;

            case 4:
                input.nextLine(); // Move cursor to next line
                System.out.print("\nEnter new username: ");
                String newUsername = input.nextLine();
                changeUsername(BankSystem.getCurrentLoggedInUser(), newUsername);
                break;

            case 5:
                input.nextLine(); // Move cursor to next line
                System.out.print("\nDo you want to enable 2FA?(Y/N): ");
                char new2FA = input.next().charAt(0);
                input.nextLine(); // Move cursor to next line
                change2FAStatus(BankSystem.getCurrentLoggedInUser(), new2FA);
                break;

            case 6:
                displayActivityLog(BankSystem.getCurrentLoggedInUser());
                break;

            case 7:
                return;

            default:
                System.out.print("*Invalid choice. Please select a valid option.");
                break;
        }
    }

    protected static void displayTransaction(final String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT product_type FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();

            String productType = null;
            if (dataSet.next()) {
                productType = dataSet.getString("product_type");
            }

            sql = "SELECT * FROM transactions WHERE user_id = (SELECT user_id FROM users WHERE username = ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            dataSet = statement.executeQuery();

            System.out.print(
                    """
                            ╔═════════════════════════════════════╗
                            ║        Transaction History          ║
                            ╚═════════════════════════════════════╝""");
            System.out.print(
                    "User: " + username +
                            "\n───────────────────────────────────────");

            while (dataSet.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Timestamp timestamp = dataSet.getTimestamp("timestamp");
                String formattedDate = sdf.format(timestamp);
                System.out.print(
                        "\nTransaction ID: " + dataSet.getString("transaction_id") +
                                "\nTransaction Type: " + dataSet.getString("transact_type") +
                                "\nAmount: $" + dataSet.getDouble("amount") +
                                "\nTimestamp: " + formattedDate);
                if (!"Savings Account".equals(productType)) {
                    System.out.print("\nDescription: " + dataSet.getString("description"));
                }
                System.out.print("\n───────────────────────────────────────");
            }

            dataSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayAnalytics(final String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                BankSystem.clearConsole();
                System.out.print(
                        """
                                ╔═════════════════════════════════════╗
                                ║           User Analytics            ║
                                ╚═════════════════════════════════════╝""");
                System.out.print(
                        "\nName: " + dataSet.getString("fname") +
                                "\n───────────────────────────────────────");
                if (Objects.equals(dataSet.getString("product_type"), "Savings Account")) {
                    System.out.print(
                            "\nTotal Net worth: " + BankSystem.calculateTotalNet() +
                                    "\nTotal Interest Earned: " + BankSystem.showInterestEarned());
                } else if (Objects.equals(dataSet.getString("product_type"), "Credit Account")) {
                    System.out.print(
                            "\nTotal Spent: " + calculateTotalSpent(username) +
                                    "\nTotal Paid: " + calculateTotalPaid(username));
                }
            }

            System.out.println("\n\nPress Enter to continue...");

            dataSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double calculateTotalNetWorth(final String username) {
        double totalNetWorth = 0;
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = (SELECT user_id FROM users WHERE username = ?) AND transact_type = 'deposit'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalNetWorth = rs.getDouble(1);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalNetWorth;
    }

    public static double calculateTotalSpent(final String username) {
        double totalSpent = 0;
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = (SELECT user_id FROM users WHERE username = ?) AND transact_type = 'withdraw'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalSpent = rs.getDouble(1);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSpent;
    }

    public static double calculateTotalPaid(final String username) {
        double totalPaid = 0;
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = (SELECT user_id FROM users WHERE username = ?) AND transact_type = 'payment'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalPaid = rs.getDouble(1);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalPaid;
    }

    public static double calculateTotalInterestEarned(final String username) {
        double totalInterestEarned = 0;
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = (SELECT user_id FROM users WHERE username = ?) AND transact_type = 'interest'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalInterestEarned = rs.getDouble(1);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalInterestEarned;
    }

    public static void processDeposit(String username) {
        System.out.print("\nEnter the amount to deposit: $");
        double depositAmount = input.nextDouble();
        input.nextLine();
        if (depositAmount <= 0.0) {
            System.out.print("*Invalid deposit amount. Please enter a positive amount.");
            return;
        } else if (Transaction.depositFunds(username, depositAmount)) {
            System.out.print("Deposit of $" + depositAmount + " successful.");
        } else {
            System.out.print("*Deposit failed. Please try again.");
        }
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void processWithdrawal(String username) {
        System.out.print("\nEnter the amount to withdraw: $");
        double withdrawAmount = input.nextDouble(); // Clear the newline character

        if (withdrawAmount <= 0.0) {
            System.out.print("*Invalid withdrawal amount. Please enter a positive amount.");
            return;
        } else if (Transaction.withdrawFunds(username, withdrawAmount)) {
            System.out.print("\nWithdrawal of $" + withdrawAmount + " successful.");
        } else {
            System.out.print("*Withdrawal failed. Please try again.");
        }
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void processPurchase(String username) {
        System.out.print("\nEnter the purchase amount: $");
        double purchaseAmount = input.nextDouble();
        input.nextLine(); // Consume leftover newline

        System.out.print("\nEnter the purchase description: ");
        String purchaseDescription = input.nextLine();

        if (purchaseAmount <= 0.0) {
            System.out.print("*Invalid transaction amount. Please enter a positive amount.");
        } else if (Transaction.makePurchase(username, purchaseAmount, purchaseDescription)) {
            System.out.print("\nPurchase of $" + purchaseAmount + " successful.");
        } else {
            System.out.print("*Purchase failed. Please try again.");
        }
        System.out.print("Press Enter to continue...");
        input.nextLine();
    }

    public static void processBills(String username) {
        System.out.print("\nEnter the bill amount: $");
        double billAmount = input.nextDouble();
        input.nextLine(); // Consume leftover newline

        System.out.print("\nEnter the bill description: ");
        String billDescription = input.nextLine();

        if (billAmount <= 0.0) {
            System.out.print("*Invalid amount. Please enter a positive amount.");
        } else if (Transaction.payBills(username, billAmount, billDescription)) {
            System.out.print("\nPayment of $" + billAmount + " successful.");
        } else {
            System.out.print("*Payment failed. Please try again.");
        }

        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    public static void applyProduct() {
        String fname, mname, lname;
        String username, password, email, phone, accounttype;
        int acctype;
        char enable2FA;
        while (true) {
            System.out.print(
                    """

                            ╔═════════════════════════════════════╗
                            ║       Product Application           ║
                            ╚═════════════════════════════════════╝""");

            System.out.print("\nEnter your given name: ");
            fname = input.nextLine();
            System.out.print("Enter your middle name: ");
            mname = input.nextLine();
            System.out.print("Enter your surname: ");
            lname = input.nextLine();
            System.out.print("Enter username: ");
            username = input.nextLine();
            boolean usernameTaken = isUsernameTaken(username);
            if (usernameTaken) {
                System.out.print("\n*Username is already taken. Please choose another one.");
                System.out.print("Press Enter to continue...");
                input.nextLine();
                BankSystem.clearConsole();
                continue;
            }

            System.out.print("Enter password: ");
            password = input.nextLine();
            System.out.print("Enter email: ");
            email = input.nextLine();
            System.out.print("Enter phone: ");
            phone = input.nextLine();
            System.out.print("\nDo you want to enable 2FA?(Y/N): ");
            enable2FA = input.next().charAt(0);
            System.out.print(
                    """

                            Pick account type:
                            1. Savings Account
                            2. Credit Account""");
            System.out.print("\nChoose your account type: ");
            acctype = input.nextInt();

            switch (acctype) {
                case 1:
                    accounttype = "Savings Account";
                    break;
                case 2:
                    accounttype = "Credit Account";
                    break;
                default:
                    System.out.print("*Invalid choice. Please select a valid option.");
                    continue;
            }

            boolean registrationSuccess = BankSystem.createUser(fname, mname, lname, username, password, email, phone,
                    enable2FA, accounttype);
            if (registrationSuccess) {
                System.out.print(
                        """
                                Registration successful!
                                Press Enter to continue...""");
                input.nextLine();
                break;
            } else {
                System.out.print("*Registration failed. Please try again.");
            }
        }
    }

    public static long generateUserID() {
        String timeString = new SimpleDateFormat("YYYYMMDDHHmm").format(date);
        String lastTwoDigitsOfTime = timeString.substring(timeString.length() - 2);

        String userID = timeString + lastTwoDigitsOfTime;
        return Long.parseLong(userID);
    }

    public static boolean isUsernameTaken(String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            return dataSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void changePassword(String username, String newPassword) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                if (dataSet.getBoolean("is2fa")) {
                    SecuritySystem.sendOTP();
                    String inputOTP = input.nextLine();
                    input.nextLine();
                    if (!SecuritySystem.verifyOTP(inputOTP)) {
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            System.err.print("\n" + e.getMessage());
                        }
                        return;
                    }
                }
                String encryptedPassword = SecuritySystem.encrypt(newPassword);
                sql = "UPDATE users SET password = ? WHERE username = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, encryptedPassword);
                statement.setString(2, getCurrentLoggedInUser());
                statement.executeUpdate();
                System.out.print("Password changed to " + newPassword + " successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeEmail(String username, String newEmail) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                if (dataSet.getBoolean("is2fa")) {
                    SecuritySystem.sendOTP();
                    String inputOTP = input.nextLine();
                    input.nextLine();
                    if (!SecuritySystem.verifyOTP(inputOTP)) {
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            System.err.print("\n" + e.getMessage());
                        }
                        return;
                    }
                }
                sql = "UPDATE users SET email = ? WHERE username = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, newEmail);
                statement.setString(2, username);
                statement.executeUpdate();
                System.out.print("Email changed to " + newEmail + " successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changePhoneNum(String username, String newPhoneNumber) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                if (dataSet.getBoolean("is2fa")) {
                    SecuritySystem.sendOTP();
                    String inputOTP = input.nextLine();
                    input.nextLine();
                    if (!SecuritySystem.verifyOTP(inputOTP)) {
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            System.err.print("\n" + e.getMessage());
                        }
                        return;
                    }
                }
                long userId = dataSet.getLong("user_id");
                sql = "UPDATE users SET phone_number = ? WHERE username = ? AND user_id = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, newPhoneNumber);
                statement.setString(2, username);
                statement.setLong(3, userId);
                statement.executeUpdate();
                System.out.print("Phone number changed to " + newPhoneNumber + " successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserAdmin(String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT is_admin FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                return dataSet.getBoolean("is_admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void changeUsername(String username, String newUsername) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                sql = "UPDATE users SET username = ? WHERE username = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, newUsername);
                statement.setString(2, username);
                statement.executeUpdate();
                System.out.print("Username changed to " + newUsername + " successfully.");

                // Get the currently logged in user's username
                String currentLoggedInUsername = BankSystem.getCurrentLoggedInUser();

                // Check if the currently logged in user is an admin
                boolean isAdmin = Admin.isUserAdmin(currentLoggedInUsername);
                if (isAdmin) {
                    // If user is admin, redirect to dashboard
                    Display.handleDashboardOptions();
                } else {
                    // Update the application's user session with the new username
                    BankSystem.setCurrentLoggedInUser(newUsername);

                    System.out.println("\n\nPlease re-login for the changes to take effect.");
                    // Logout and redirect to login page
                    Display.logout(newUsername); // Use newUsername instead of currentLoggedInUsername
                    Display.loginUser();
                }
            } else {
                System.out.println("User with username " + username + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void change2FAStatus(String username, char new2FA) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet dataSet = statement.executeQuery();
            if (dataSet.next()) {
                if (dataSet.getBoolean("is2fa")) { // Changed '2FAStatus' to 'is2fa'
                    SecuritySystem.sendOTP();
                    String inputOTP = input.nextLine();
                    input.nextLine();
                    if (!SecuritySystem.verifyOTP(inputOTP)) {
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            System.err.print("\n" + e.getMessage());
                        }
                        return;
                    }
                }

                boolean new2FAStatus = SecuritySystem.enable2FA(new2FA);

                sql = "UPDATE users SET is2fa = ? WHERE username = ?";
                statement = conn.prepareStatement(sql);
                statement.setBoolean(1, new2FAStatus);
                statement.setString(2, username);
                statement.executeUpdate();

                String show2FAStatus = new2FAStatus ? "Enabled" : "Disabled";
                System.out.print("Two Factor Authentication: " + show2FAStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void askHelp(String username) {
        System.out.print("Enter your message: ");
        String message = input.nextLine();

        try {
            Connection conn = BankSystem.getConnection();
            String sql = "INSERT INTO help_resources (user_id, hr_type, hr_description) VALUES (?, 'Help', ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            // Get the user_id of the user
            long userId = User.getUserId(username);
            if (userId == -1) {
                System.out.println("User with username " + username + " not found.");
                return;
            }

            statement.setLong(1, userId);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAdmin() { // CONVERT Database
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT is_admin FROM users WHERE user_id = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());

            dataSet = statement.executeQuery();
            if (dataSet.next()) {
                return dataSet.getBoolean("is_admin");
            }
        } catch (SQLException e) {
            System.err.print("Error in Data Retrieving: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }

        return false;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static boolean isCustomerService() { // CONVERT Database
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT is_customerservice FROM users WHERE user_id = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());

            dataSet = statement.executeQuery();
            if (dataSet.next()) {
                return dataSet.getBoolean("is_customerservice");
            }
        } catch (SQLException e) {
            System.err.print("Error in Data Retrieving: " + e.getMessage());
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }

        return false;
    }

    protected static void displayHelpHistory(long userID) {
        PreparedStatement statement = null;
        ResultSet dataSet = null;
        boolean helpFound = false;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT * FROM help_resources WHERE user_id = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, userID);

            dataSet = statement.executeQuery();

            if (dataSet.next()) {
                long user_id = dataSet.getLong("user_id");
                if (Objects.equals(user_id, userID)) {
                    System.out.print(
                            """

                                    ╔═════════════════════════════════════════════════════════════╗
                                    ║                        Help History                         ║
                                    ╚═════════════════════════════════════════════════════════════╝
                                    ───────────────────────────────────────────────────────────────""");
                    System.out.print(
                            "\nHelp ID: " + dataSet.getLong("hr_id") +
                                    "\nType: " + dataSet.getString("hr_type") +
                                    "\nDescription: " + dataSet.getString("hr_description"));
                    String feedback = dataSet.getString("feedback");
                    if (feedback == null || feedback.isEmpty()) {
                        feedback = "No feedback yet.";
                    }
                    System.out.print(
                            "\nFeedback: " + feedback +
                                    "\n───────────────────────────────────────────────────────────────");

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
}
