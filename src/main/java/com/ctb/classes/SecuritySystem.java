package com.ctb.classes;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ctb.classes.BankSystem.users;
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

            while(hashPass.length() < 32) {
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

    // Attempts to log in and tracks failed attempts.
    protected static boolean attemptLogin(final String password, final String verifyPass)
    {
        if (Objects.equals(verifyPass, password))
            return true;

        attempts++;
        lastAttempt = System.currentTimeMillis();
        return false;
    }

    protected static void sendOTP()
    {
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
        Optional<User> user = users.stream()
                .filter(u -> User.getUsername().equals(username))
                .findFirst();

        if (user.isEmpty()) {
            return false;
        }

        String decryptedPass = encrypt(password);

        if (!attemptLogin(decryptedPass, user.get().getPassword())) {
            return false;
        }

        for (Profile profile : user.get().userProfile) {
            if (profile.get2FAStatus()) {
                System.out.print("\n---Sending an OTP for 2 Factor Authentication---");
                sendOTP();

                String inputOTP;
                System.out.print("\nEnter your OTP: ");
                inputOTP = new Scanner(System.in).nextLine();

                if (!verifyOTP(inputOTP)) {
                    System.out.print("\n*Incorrect OTP. Timeout for 30 seconds...");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        System.err.print("\n" + e.getMessage());
                    }
                    return false;
                }
            }
        }
        Session.saveSession(username, "Login");
        return true;
    }

    protected static String getCurrentDate() {
        LocalDate currTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return currTime.format(formatter);
    }

    protected static boolean securityStatus(final boolean status) {
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
