package com.ctb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class Transaction {
    private static final Scanner input = new Scanner(System.in);
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private String transactionID;
    private String transactionType;
    private String description;
    private double amount;
    private long timeStamp;

    /*----------------------Setter Methods----------------------*/
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /*----------------------Getter Methods----------------------*/
    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    /*----------------------Class Methods----------------------*/

    protected static String generateTransactionID() {
        {
            long time = calendar.getTimeInMillis();
            int randomNumber = rand.nextInt();
            String timeString = Long.toString(time);
            String randomNumberString = Integer.toString(randomNumber);

            return "TXN" + timeString + randomNumberString;
        }
    }

    protected static boolean depositFunds(String username, double amount) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (handleVerification(username))
                    return false;

                String transactionID = generateTransactionID();
                long timeStampMillis = Calendar.getInstance().getTimeInMillis();
                java.sql.Timestamp timeStamp = new java.sql.Timestamp(timeStampMillis);

                sql = "INSERT INTO transactions (transaction_id, transact_type, amount, timestamp, user_id) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, transactionID);
                pstmt.setString(2, "Deposit");
                pstmt.setDouble(3, amount);
                pstmt.setTimestamp(4, timeStamp);
                pstmt.setLong(5, rs.getLong("user_id"));
                pstmt.executeUpdate();

                sql = "UPDATE users SET balance = balance + ? WHERE username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setString(2, username);
                pstmt.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean handleVerification(String username) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getBoolean("is2fa")) {
                System.out.print("\nSending an OTP for 2 Factor Authentication.");
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
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected static boolean withdrawFunds(String username, double amount) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (handleVerification(username))
                    return false;

                if (amount <= 0.0) {
                    System.out.print("*Invalid withdrawal amount. Please enter a positive amount.");
                    return false;
                }

                if (rs.getDouble("balance") >= amount) {
                    String transactionID = generateTransactionID();
                    long timeStampMillis = Calendar.getInstance().getTimeInMillis();
                    java.sql.Timestamp timeStamp = new java.sql.Timestamp(timeStampMillis);

                    sql = "INSERT INTO transactions (transaction_id, transact_type, amount, timestamp, user_id) VALUES (?, ?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, transactionID);
                    pstmt.setString(2, "Withdrawal");
                    pstmt.setDouble(3, amount);
                    pstmt.setTimestamp(4, timeStamp);
                    pstmt.setLong(5, rs.getLong("user_id"));
                    pstmt.executeUpdate();

                    sql = "UPDATE users SET balance = balance - ? WHERE username = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setDouble(1, amount);
                    pstmt.setString(2, username);
                    pstmt.executeUpdate();

                    return true;
                } else {
                    System.out.print("\n*Insufficient balance. Withdrawal failed.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.print("*User not found. Withdrawal failed.");
        return false;
    }

    protected static boolean makePurchase(String username, double amount, String purchaseDescription) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (handleVerification(username))
                    return false;

                if (amount <= 0.0) {
                    System.out.print("*Invalid purchase amount. Please enter a positive amount.");
                    return false;
                }

                if (rs.getDouble("balance") - amount < -5000.0) {
                    System.out.print("*Insufficient credit limit. Purchase failed.");
                    return false;
                }

                String transactionID = generateTransactionID();
                long timeStampMillis = Calendar.getInstance().getTimeInMillis();
                java.sql.Timestamp timeStamp = new java.sql.Timestamp(timeStampMillis);

                sql = "INSERT INTO transactions (transaction_id, transact_type, amount, timestamp, user_id, description) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, transactionID);
                pstmt.setString(2, "Purchase");
                pstmt.setDouble(3, amount);
                pstmt.setTimestamp(4, timeStamp);
                pstmt.setLong(5, rs.getLong("user_id"));
                pstmt.setString(6, purchaseDescription);
                pstmt.executeUpdate();

                sql = "UPDATE users SET balance = balance - ? WHERE username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setString(2, username);
                pstmt.executeUpdate();

                System.out.print(
                        "Purchase of $" + amount + " successful. " +
                                "\nDescription: " + purchaseDescription);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.print("*User not found. Purchase failed.");
        return false;
    }

    protected static boolean payBills(String username, double amount, String billDescription) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (handleVerification(username))
                    return false;

                if (amount <= 0.0) {
                    System.out.print("*Invalid bill amount. Please enter a positive amount.");
                    return false;
                }

                String transactionID = generateTransactionID();
                long timeStampMillis = Calendar.getInstance().getTimeInMillis();
                java.sql.Timestamp timeStamp = new java.sql.Timestamp(timeStampMillis);

                sql = "INSERT INTO transactions (transaction_id, transact_type, amount, timestamp, user_id, description) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, transactionID);
                pstmt.setString(2, "BP");
                pstmt.setString(2, "Bill Payment");
                pstmt.setDouble(3, amount);
                pstmt.setTimestamp(4, timeStamp);
                pstmt.setLong(5, rs.getLong("user_id"));
                pstmt.setString(6, billDescription);
                pstmt.executeUpdate();

                sql = "UPDATE users SET balance = balance + ? WHERE username = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setString(2, username);
                pstmt.executeUpdate();

                System.out.print(
                        "Bill payment of $" + amount + " successful. " +
                                "\nDescription: " + billDescription);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.print("*User not found. Bill payment failed.");
        return false;
    }
}