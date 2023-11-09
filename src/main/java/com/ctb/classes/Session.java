package com.ctb.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Session extends User{ //TODO: transfer session methods to this class
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private static final Date currentTime = new Date();
    private static Session instance;
    private User userData;
    private String sessionID;
    private long timeStamp;

    private Session(User userData) {
        this.userData = userData;
    }

    protected Session() {}

    /*----------------------Setter Methods----------------------*/
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /*----------------------Getter Methods----------------------*/
    public static Session getInstance(User userData) { //TODO: implement this
        if(instance == null) {
            instance = new Session(userData);
        }

        return instance;
    }
    public void setSessionID(String sessionID) {this.sessionID = sessionID;}
    public long getTimeStamp() {return timeStamp;}
    public String getSessionID() {return sessionID;}

    /*----------------------Class Methods----------------------*/
    protected static String generateSessionID(String sessionType) { //TODO: change structure String -> long
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
        //CONVERT: List -> Database
        long currentTimeInSeconds = currentTime.getTime() / 1000;
        for (User user : BankSystem.users) {
            if (Objects.equals(getUsername(), username)) {
                Session session = new Session();
                session.sessionID = generateSessionID(sessionType);
                session.setUsername(username);
                session.setTimeStamp(currentTimeInSeconds);
                user.userSessions.add(session);

                BankSystem.saveDataToFile();
                SecuritySystem.auditLog(true);
                return;
            }
        }
        SecuritySystem.auditLog(false);
    }

    public User getUserData() {
        return userData;
    }
}
