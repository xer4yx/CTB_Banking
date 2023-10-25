package com.ctb.classes;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Session extends User{
    private static final Date currentTime = new Date();
    private String sessionID;
    private long timeStamp;

    public void setSessionID(String sessionID) {this.sessionID = sessionID;}
    public void setSessionType(String sessionType) {
    }

    public long getTimeStamp() {return timeStamp;}
    public String getSessionID() {return sessionID;}


    /*----------------------Class Methods----------------------*/
    protected static String generateSessionID(String sessionType) {
        if (Objects.equals(sessionType, "Login")) {
            return "LGN" + Instant.now().getEpochSecond();
        }

        if (Objects.equals(sessionType, "Logout")) {
            return "LGT" + Instant.now().getEpochSecond();
        }

        return "SSN" + Instant.now().getEpochSecond();
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
