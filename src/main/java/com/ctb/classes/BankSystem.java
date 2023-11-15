package com.ctb.classes;

import com.ctb.exceptions.DataInsertionException;
import com.ctb.exceptions.DataRetrievalException;
import com.ctb.exceptions.InvalidProductTypeException;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class BankSystem {
    static String url = "jdbc:mysql://localhost:3306/ctb_banking";
    static String userDB = "root";
    static String passwordDB = "password";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static final Scanner input = new Scanner(System.in);
    private static long currentUserID;
    private static String currentLoggedInUser;
    private static String currentProductType;
    private static long currentBalance;
    private static String dataFilePath;

    public static final List<User> users = new LinkedList<>();
    public static final List<Profile> profiles = new LinkedList<>();
    private static final List<Transaction> transactionHistory = new LinkedList<>();
    private static final List<ProductApplication> productApplications = new LinkedList<>();
    private static final List<Session> sessions = new LinkedList<>();
    private static final List<Dashboard> dashboards = new LinkedList<>();

    /*----------------------Setter Methods----------------------*/
    protected static void setCurrentUserID(long currentUserID) {
        BankSystem.currentUserID = currentUserID;
    }

    protected static void setCurrentLoggedInUser(String username) {
        currentLoggedInUser = username;
    }

    protected static void setCurrentProductType(String productType) {
        try {
            if (!isValidProductType(productType)) {
                throw new InvalidProductTypeException("Invalid Product Type");
            }

            currentProductType = productType;
        } catch (InvalidProductTypeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /*----------------------Getter Methods----------------------*/
    protected static long getCurrentUserID() {
        return currentUserID;
    }

    protected static String getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    protected static String getCurrentProductType(String username) {
        return currentProductType; // TODO: Set up database
    }

    protected static double getCurrentBalance(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);

            String query = "SELECT balance FROM users WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            dataSet = statement.executeQuery();

            if (dataSet.next()) {
                return dataSet.getDouble("balance");
            } else {
                throw new DataRetrievalException("Balance Irretrievable");
            }

        } catch (SQLException e) {
            System.err.print("Error on Data Retrieval: " + e.getMessage());
        } finally {
            closeResources(connection, statement, dataSet);
        }

        return -1.0;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, userDB, passwordDB);
    }

    /*----------------------Class Methods----------------------*/
    public static void forgotPassword() {
        System.out.print(
                """
                        ╭────────────────────────────────────────────────────────────╮
                        │                     Forgot Password                        │
                        ╰────────────────────────────────────────────────────────────╯""");
        char choice;
        System.out.print("\nEnter your email: ");
        String email = input.nextLine();
        boolean emailFound = false;

        // TODO: Separate method for handling forgot pass (should be inside User)
        // CONVERT: List -> Database

        for (User user : BankSystem.users) {
            for (Profile profile : user.userProfile) {
                if (Objects.equals(profile.getEmail(), email)) {
                    System.out.print("---Email found!---");
                    System.out.print("\nSending an OTP for " + profile.getEmail() + " 2 Factor Authentication.");
                    SecuritySystem.sendOTP();
                    System.out.print("\nEnter your OTP: ");
                    String inputOTP = input.nextLine();
                    if (!SecuritySystem.verifyOTP(inputOTP)) {
                        System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            System.err.print(e.getMessage());
                        }
                        return;
                    }
                    System.out.print(
                            """
                                    ──────────────────────────────────────────────────────────────
                                    Enter new password:\s
                                    """);
                    String newpass = input.nextLine();
                    User.changePassword(User.getUsername());
                    System.out.print("---Password changed successfully!---");
                    emailFound = true;
                }
            }
        }

        if (!emailFound) {
            System.out.print("\n*Email not found. Please try again.");
        }
    }

    protected static void closeResources(Connection connection, PreparedStatement statement, ResultSet dataSet) {
        try {
            if (dataSet != null) {
                dataSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void closeResources(Connection connection, PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static double showInterestEarned() {
        double interestRate = 0.05;
        double interestEarned = 0;
        double principal = 0;

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);

            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Deposit");

            dataSet = statement.executeQuery();
            while (dataSet.next()) {
                principal += dataSet.getDouble("amount");
                Date transactionDate = dataSet.getDate("timestamp");
                long diffInMillis = Math.abs(now.getTime() - transactionDate.getTime());
                long years = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
                double amount = principal * Math.pow(1 + interestRate, years);
                double interest = amount - principal;

                interestEarned += interest;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, dataSet);
        }

        return interestEarned;
    }

    protected static double calculateTotalPaid() {
        double totalPaid = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Bill Payment");

            dataSet = statement.executeQuery();
            while (dataSet.next()) {
                totalPaid += dataSet.getDouble("amount");
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, dataSet);
        }

        return totalPaid;
    }

    protected static double calculateTotalSpent() {
        double totalSpent = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ?";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Purchase");

            dataSet = statement.executeQuery();
            while (dataSet.next()) {
                totalSpent += dataSet.getDouble("amount");
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, dataSet);
        }

        return totalSpent;
    }

    protected static double calculateTotalNet() {
        double totalNet = 0;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
            String query = "SELECT amount FROM transactions WHERE user_id = ? AND transact_type = ? ";

            statement = connection.prepareStatement(query);
            statement.setLong(1, getCurrentUserID());
            statement.setString(2, "Deposit");

            dataSet = statement.executeQuery();
            while (dataSet.next()) {
                totalNet += dataSet.getDouble("amount");
            }

            dataSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, dataSet);
        }

        return totalNet;
    }

    @Deprecated
    protected static double showInterestEarned(String username) { // TODO: delete obsolete
        double interestRate = 0.05; // Annual interest rate
        double interestEarned = 0;

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        for (User user : users) {
            if (User.getUsername().equals(username)) {
                for (Transaction transaction : user.userTransaction) {
                    if (transaction.getTransactionType().equals("Deposit")) {
                        // Calculate interest for this deposit
                        double principal = transaction.getAmount();
                        long diffInMillis = Math.abs(now.getTime() - transaction.getTimeStamp());
                        long years = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
                        double amount = principal * Math.pow(1 + interestRate, years);
                        double interest = amount - principal;

                        // Add to total interest earned
                        interestEarned += interest;
                    }
                }
            }
        }

        return interestEarned;
    }

    protected static boolean isValidProductType(String productType) {
        List<String> validProductTypes = new LinkedList<>(Arrays.asList("Savings Account", "Credit Account"));
        return validProductTypes.contains(productType);
    }

    protected static boolean isAdmin(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
            String query = "SELECT is_admin FROM users WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            dataSet = statement.executeQuery();
            return dataSet.getBoolean("is_admin");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, dataSet);
        }
        return false;
    }

    protected static boolean isCustomerService(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);
            String query = "SELECT is_customerservice FROM users WHERE username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            dataSet = statement.executeQuery();
            return dataSet.getBoolean("is_customerservice");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, dataSet);
        }
        return false;
    }

    protected static boolean createUser(String fname, String mname, String lname, String username, String password,
            String email, String phoneNum, char twoFA, String productType) {
        if (User.isUsernameTaken(username)) {
            System.out.print("Username is already taken. Please choose another one.");
            return false;
        }

        User newUser = new User();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.print("Driver error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(url, userDB, passwordDB);

            String query = "INSERT INTO users (user_id, fname, mname, lname, username, password, email, phone_number, "
                    +
                    "is2fa, is_admin, is_customerservice, product_type, balance) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                statement = connection.prepareStatement(query);

                statement.setLong(1, newUser.generateUserID());
                statement.setString(2, fname);
                statement.setString(3, mname);
                statement.setString(4, lname);
                statement.setString(5, username);
                statement.setString(6, SecuritySystem.encrypt(password));
                statement.setString(7, email);
                statement.setString(8, phoneNum);
                statement.setBoolean(9, SecuritySystem.enable2FA(twoFA));
                statement.setBoolean(10, false);
                statement.setBoolean(11, false);
                statement.setString(12, productType);
                statement.setDouble(13, 0.00);

                int affectedRows = statement.executeUpdate();
                System.out.println("Number of affected rows: " + affectedRows); // [DEBUGGER]
            } catch (SQLException e) {
                throw new DataInsertionException("Error while inserting data into users table: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.print("Error connecting to database: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            closeResources(connection, statement);
        }

        System.out.print("\nUser account created successfully.");
        return true;
    }

    public static void clearConsole() {

    }

    public static void saveDataToFile() {
    }
}
