package com.alliot.inventory.repository;

public enum ErrorType {
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    SERVER_ERROR,
    NO_INTERNET,
    TIMEOUT,
    CONNECTION_FAILED,
    UNKNOWN;

    private int httpCode;
    private String rawMessage;

    public int getHttpCode() { return httpCode; }
    public String getRawMessage() { return rawMessage; }

    public static ErrorType fromHttpCode(int code) {
        ErrorType type;
        switch (code) {
            case 401: type = UNAUTHORIZED; break;
            case 403: type = FORBIDDEN; break;
            case 404: type = NOT_FOUND; break;
            case 500: type = SERVER_ERROR; break;
            default:  type = UNKNOWN; break;
        }
        type.httpCode = code;
        return type;
    }

    public static ErrorType fromThrowable(Throwable t) {
        ErrorType type;
        if (t instanceof java.net.UnknownHostException) {
            type = NO_INTERNET;
        } else if (t instanceof java.net.SocketTimeoutException) {
            type = TIMEOUT;
        } else if (t instanceof java.net.ConnectException) {
            type = CONNECTION_FAILED;
        } else {
            type = UNKNOWN;
        }
        type.rawMessage = t.getMessage();
        return type;
    }
}
