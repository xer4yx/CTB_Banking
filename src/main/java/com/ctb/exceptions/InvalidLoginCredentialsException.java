package com.ctb.exceptions;

public class InvalidLoginCredentialsException extends Exception {
    public InvalidLoginCredentialsException(String reason) {
        super("Invalid Login Credentials: " + reason);
    }
}

