package com.ctb.exceptions;
import java.sql.SQLException;

public class DataInsertionException extends SQLException {

    public DataInsertionException(String message) {
        super(message);
    }

    public DataInsertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInsertionException(Throwable cause) {
        super(cause);
    }
}