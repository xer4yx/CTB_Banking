package com.ctb.classes;

import com.ctb.exceptions.InvalidLoginCredentialsException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.*;

import static java.lang.Character.toUpperCase;

class SecuritySystem {
    private static final Scanner input = new Scanner(System.in);
    static int attempts;
    static long lastAttempt;
    static String OTP;

    protected static String encrypt(String password) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md5.digest(password.getBytes());
            BigInteger signum = new BigInteger(1, messageDigest);
            StringBuilder hashPass = new StringBuilder(signum.toString(16));

            while (hashPass.length() < 32) {
                hashPass.insert(0, "0");
            }
            return hashPass.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String decrypt(String hashedPassword) {
        try {
            byte[] bytesOfMessage = new BigInteger(hashedPassword, 16).toByteArray();
            return new String(bytesOfMessage, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateOTP() {
        int length = 6;
        Random rand = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++)
            otp.append(rand.nextInt(10));

        return otp.toString();
    }

    protected static boolean canAttempt() {
        long currTime = Calendar.getInstance().getTimeInMillis();
        if (attempts > 3 && (currTime - lastAttempt) < 30000)
            return false;

        if ((currTime - lastAttempt) >= 30000)
            attempts = 0;

        lastAttempt = currTime;
        return true;
    }

    @Deprecated
    protected static boolean attemptLogin(String password, String verifyPass) { // TODO: delete this
        if (Objects.equals(verifyPass, password))
            return true;

        attempts++;
        lastAttempt = System.currentTimeMillis();
        return false;
    }

    protected static void sendOTP() {
        OTP = generateOTP();
        System.out.print("\nYour One-time Password is: " + OTP + ". Do not give or send this to other people.");
    }

    protected static boolean verifyOTP(final String onetimepass) {
        if (Objects.equals(OTP, onetimepass)) {
            attempts = 0;
            return true;
        }
        return false;
    }

    protected static boolean enable2FA(final char answer) {
        return toUpperCase(answer) == 'Y';
    }

    public static boolean authenticateUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet dataSet = null;

        try {
            connection = DriverManager.getConnection(BankSystem.url, BankSystem.userDB, BankSystem.passwordDB);
            String query = "SELECT user_id, username, password, is2fa, product_type FROM users WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            dataSet = statement.executeQuery();

            if (dataSet.next()) {
                long user_id = dataSet.getLong("user_id");
                String name = dataSet.getString("username");
                String pass = dataSet.getString("password");
                boolean twoFA = dataSet.getBoolean("is2fa");
                String prodType = dataSet.getString("product_type");

                String formPass = encrypt(password);

                if (!name.equals(username)) {
                    throw new InvalidLoginCredentialsException("\nInvalid username");
                } else if (!pass.equals(formPass)) {
                    throw new InvalidLoginCredentialsException("\nInvalid password");
                } else if (!BankSystem.isValidProductType(prodType)) {
                    throw new InvalidLoginCredentialsException("\nInvalid product type");
                }

                if (twoFA) {
                    if (perform2FA()) {
                        return false;
                    }
                }

                BankSystem.setCurrentUserID(user_id);
                BankSystem.setCurrentLoggedInUser(name);
                BankSystem.setCurrentProductType(prodType);

                return true;
            } else {
                throw new InvalidLoginCredentialsException("\nUser not found or not yet registered");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidLoginCredentialsException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            BankSystem.closeResources(connection, statement, dataSet);
        }
    }

    private static boolean perform2FA() {
        System.out.print("\n---Sending an OTP for 2 Factor Authentication---");
        sendOTP();

        String inputOTP;
        System.out.print("\nEnter your OTP: ");
        inputOTP = new Scanner(System.in).nextLine();

        if (!verifyOTP(inputOTP)) {
            System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.print("\n" + e.getMessage());
            }
            return true;
        }
        return false;
    }

    @Deprecated
    protected static String getCurrentDate() { // TODO: delete this
        LocalDate currTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return currTime.format(formatter);
    }

    protected static boolean securityStatus(final boolean status) {
        // TODO: add functionalities on the code
        return status;
    }

    public static void auditLog(boolean status) {
        try {
            String existingLogs = "[]";
            try {
                existingLogs = new String(Files.readAllBytes(Paths.get("audit_log.json")));
            } catch (IOException e) {
                System.err.print(e.getMessage());
            }

            JSONArray auditLogs = new JSONArray(existingLogs);

            // Get the current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = dateFormat.format(new Date());

            // Check security status and update statusResult
            boolean currentStatus = securityStatus(status);
            String statusResult = currentStatus ? "PASSED" : "FAILED";

            // Create a new JSONObject and add the current date and statusResult
            JSONObject newLog = new JSONObject();
            newLog.put("timestamp", currentDate);
            newLog.put("status", statusResult);

            // Add the new JSONObject to the JSONArray
            auditLogs.put(newLog);

            // Write the updated JSONArray to the audit log file
            try (FileWriter file = new FileWriter("audit_log.json")) {
                file.write(auditLogs.toString());
                file.flush();
            }
        } catch (IOException | org.json.JSONException e) {
            System.err.print("\n" + e.getMessage());
        }
    }

}
