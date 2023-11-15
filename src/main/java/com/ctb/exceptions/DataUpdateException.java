package com.ctb.exceptions;

import java.sql.SQLException;

public class DataUpdateException extends SQLException {
    public DataUpdateException(String message) {
        super(message);
    }

    public DataUpdateException(Throwable cause) {
        super(cause);
    }

    public DataUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
