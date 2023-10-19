package com.ctb.classes;

import java.time.Instant;

public class Session{
    private String sessionID;
    private String username;
    private final long timeStamp = Instant.now().getEpochSecond();
}
