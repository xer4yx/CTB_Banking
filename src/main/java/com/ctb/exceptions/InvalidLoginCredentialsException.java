package com.ctb.exceptions;

public class InvalidLoginCredentialsException extends Exception {
    public InvalidLoginCredentialsException(String reason) {
        super(reason);
    }

    public InvalidLoginCredentialsException(Throwable cause) {
        super(cause);
    }

    public InvalidLoginCredentialsException(String reason, Throwable cause) {
        super(reason + ": " + cause);
    }
}

