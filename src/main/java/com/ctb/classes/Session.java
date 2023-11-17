package com.ctb.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class Session extends User { // TODO: transfer session methods to this class
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private static Session instance;
    private User userData;
    private String sessionID;
    private long timeStamp;

    private Session(User userData) {
        this.userData = userData;
    }

    protected Session() {
    }

    /*----------------------Setter Methods----------------------*/
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /*----------------------Getter Methods----------------------*/
    public static Session getInstance(User userData) { // TODO: implement this
        if (instance == null) {
            instance = new Session(userData);
        }

        return instance;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getSessionID() {
        return sessionID;
    }

    /*----------------------Class Methods----------------------*/
    protected static String generateSessionID(String sessionType) { // TODO: change structure String -> long
        long time = calendar.getTimeInMillis();
        int randomNumber = rand.nextInt();
        String timeString = Long.toString(time);
        String randomNumberString = Integer.toString(randomNumber);
        if (Objects.equals(sessionType, "Login")) {
            return "LGN" + timeString + randomNumberString;
        }

        if (Objects.equals(sessionType, "Logout")) {
            return "LGT" + timeString + randomNumberString;
        }

        return "SSN" + timeString + randomNumberString;
    }

    public static void saveSession(final String username, final String sessionType) {
        try {
            Connection conn = BankSystem.getConnection();
            String sql = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                long userId = rs.getLong("user_id");
                String sessionId = generateSessionID(sessionType);
                long currentTimeInSeconds = System.currentTimeMillis() / 1000;
                java.sql.Timestamp timeStamp = new java.sql.Timestamp(currentTimeInSeconds);

                sql = "INSERT INTO sessions (session_id, timestamp, user_id) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, sessionId);
                pstmt.setTimestamp(2, timeStamp);
                pstmt.setLong(3, userId);
                pstmt.executeUpdate();

                BankSystem.saveDataToFile();
                SecuritySystem.auditLog(true);
            } else {
                SecuritySystem.auditLog(false);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserData() {
        return userData;
    }
}
