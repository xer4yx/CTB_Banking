package com.ctb.classes;

import java.time.Instant;

public class Session{
    private String sessionID;
    private String username;
    private final long timeStamp = Instant.now().getEpochSecond();

    /*----------------------Class Methods----------------------*/
    protected static void generateSessionID(final String username) {
        if (sessiontype == "Login")
        {
            return "LGN" + to_string(time(nullptr)) + to_string(rand());
        }
        if (sessiontype == "Logout")
        {
            return "LGT" + to_string(time(nullptr)) + to_string(rand());
        }
        else
        {
            return "SSN" + to_string(time(nullptr)) + to_string(rand());
        }
    }
    protected static void setCurrentSessionID(final String sessionID) {currentSessionID = sessionID;}
    protected static void saveSession(final String username, final String sessionType) {
        for (User &user : users)
        {
            if (user.username == username)
            {
                Session session;
                session.sessionID = generateSessionID(sessiontype);
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
}
