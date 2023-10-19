package com.ctb.interfaces;

public interface SessionInterface {
    void generateSessionID(final String username);
    void setCurrentSessionID(final String sessionID);
    void saveSession(final String username, final String sessionType);

}
