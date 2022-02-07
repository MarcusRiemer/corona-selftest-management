package de.akra.coronatestmanagement;

public class CoronaTestManagementException extends RuntimeException {
    public CoronaTestManagementException() {
    }

    public CoronaTestManagementException(String message) {
        super(message);
    }

    public CoronaTestManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoronaTestManagementException(Throwable cause) {
        super(cause);
    }

    public CoronaTestManagementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
