package com.ctb.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Session extends User{
    private static final Calendar calendar = Calendar.getInstance();
    private static final Random rand = new Random();
    private static final Date currentTime = new Date();
    private String sessionID;
    private long timeStamp;

    public void setSessionID(String sessionID) {this.sessionID = sessionID;}
    public long getTimeStamp() {return timeStamp;}
    public String getSessionID() {return sessionID;}


    /*----------------------Class Methods----------------------*/
    protected static String generateSessionID(String sessionType) {
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

    protected static void saveSession(final String username, final String sessionType) {
        long currentTimeInSeconds = currentTime.getTime() / 1000;
        for (User user : BankSystem.users)
        {
            if (Objects.equals(getUsername(), username))
            {
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

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
