package com.ctb.classes;

import java.time.Instant;
import java.util.Objects;

public class Session extends User{
    private String sessionID;
    private String sessionType;
    private long timeStamp;

    public void setSessionID(String sessionID) {this.sessionID = sessionID;}
    public void setSessionType(String sessionType) {this.sessionType = sessionType;}

    public long getTimeStamp() {return timeStamp;}
    public String getSessionID() {return sessionID;}


    /*----------------------Class Methods----------------------*/
    protected String generateSessionID() {
        if (Objects.equals(sessionType, "Login")) {
            return "LGN" + Instant.now().getEpochSecond();
        }

        if (Objects.equals(sessionType, "Logout")) {
            return "LGT" + Instant.now().getEpochSecond();
        }

        return "SSN" + Instant.now().getEpochSecond();
    }

    protected static void saveSession(final String username, final String sessionType) {
        for (User user : users)
        {
            if (user.username == username)
            {
                Session session;
                session.sessionID = generateSessionID(sessionType);
                session.username = username;
                session.timestamp = time(nullptr);
                user.sessions.push_back(session);

                // Save the updated user data to the file
                saveDataToFile();
                system.auditLog(true);
                return; // Exit the function once the session is saved for the user.
            }
        }
        // If we've reached here, it means the user wasn't found.
        system.auditLog(false);
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
