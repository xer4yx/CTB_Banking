package com.ctb.exceptions;

import java.sql.SQLException;

public class DataRetrievalException extends SQLException {
    public DataRetrievalException(String message) {
        super(message);
    }

    public DataRetrievalException(Throwable cause) {
        super(cause);
    }

    public DataRetrievalException(String message, Throwable cause) {
        super(message + ": " + cause);
    }
}
