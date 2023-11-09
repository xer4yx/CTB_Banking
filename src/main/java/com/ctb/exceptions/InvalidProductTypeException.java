package com.ctb.exceptions;

public class InvalidProductTypeException extends Exception{
    public InvalidProductTypeException(String message) {
        super(message);
    }

    public InvalidProductTypeException(Throwable cause) {
        super(cause);
    }

    public InvalidProductTypeException(String message, Throwable cause) {
        super(message + ": " + cause);
    }
}
