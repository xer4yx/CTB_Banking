package com.ctb.exceptions;

import java.sql.SQLException;

public class DataDeletionException extends SQLException {
    public DataDeletionException(String message) {
        super(message);
    }

    public DataDeletionException(Throwable cause) {
        super(cause);
    }

    public DataDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
