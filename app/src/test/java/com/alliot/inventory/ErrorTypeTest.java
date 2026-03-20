package com.alliot.inventory;

import com.alliot.inventory.repository.ErrorType;

import org.junit.Test;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class ErrorTypeTest {

    @Test
    public void fromHttpCode_401_returnsUnauthorized() {
        ErrorType type = ErrorType.fromHttpCode(401);
        assertEquals(ErrorType.UNAUTHORIZED, type);
        assertEquals(401, type.getHttpCode());
    }

    @Test
    public void fromHttpCode_403_returnsForbidden() {
        assertEquals(ErrorType.FORBIDDEN, ErrorType.fromHttpCode(403));
    }

    @Test
    public void fromHttpCode_404_returnsNotFound() {
        assertEquals(ErrorType.NOT_FOUND, ErrorType.fromHttpCode(404));
    }

    @Test
    public void fromHttpCode_500_returnsServerError() {
        assertEquals(ErrorType.SERVER_ERROR, ErrorType.fromHttpCode(500));
    }

    @Test
    public void fromHttpCode_unknown_returnsUnknownWithCode() {
        ErrorType type = ErrorType.fromHttpCode(502);
        assertEquals(ErrorType.UNKNOWN, type);
        assertEquals(502, type.getHttpCode());
    }

    @Test
    public void fromThrowable_unknownHost_returnsNoInternet() {
        ErrorType type = ErrorType.fromThrowable(new UnknownHostException("api.alliot.dev"));
        assertEquals(ErrorType.NO_INTERNET, type);
        assertNotNull(type.getRawMessage());
    }

    @Test
    public void fromThrowable_socketTimeout_returnsTimeout() {
        ErrorType type = ErrorType.fromThrowable(new SocketTimeoutException("read timed out"));
        assertEquals(ErrorType.TIMEOUT, type);
    }

    @Test
    public void fromThrowable_connectException_returnsConnectionFailed() {
        ErrorType type = ErrorType.fromThrowable(new ConnectException("Connection refused"));
        assertEquals(ErrorType.CONNECTION_FAILED, type);
    }

    @Test
    public void fromThrowable_genericException_returnsUnknown() {
        ErrorType type = ErrorType.fromThrowable(new RuntimeException("something broke"));
        assertEquals(ErrorType.UNKNOWN, type);
        assertEquals("something broke", type.getRawMessage());
    }
}


