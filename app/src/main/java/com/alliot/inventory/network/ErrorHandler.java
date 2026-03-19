package com.alliot.inventory.network;

import android.content.Context;

import com.alliot.inventory.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.util.Log;

public final class ErrorHandler {

    private static final String TAG = "ErrorHandler";

    private ErrorHandler() {}

    public static String fromHttpCode(Context context, int code) {
        Log.w(TAG, "HTTP error: " + code);
        switch (code) {
            case 401: return context.getString(R.string.error_unauthorized);
            case 403: return context.getString(R.string.error_forbidden);
            case 404: return context.getString(R.string.error_not_found);
            case 500: return context.getString(R.string.error_server);
            default:  return context.getString(R.string.error_server_code, code);
        }
    }

    public static String fromThrowable(Context context, Throwable t) {
        Log.e(TAG, "Network error", t);
        if (t instanceof UnknownHostException) {
            return context.getString(R.string.error_no_internet);
        } else if (t instanceof SocketTimeoutException) {
            return context.getString(R.string.error_timeout);
        } else if (t instanceof ConnectException) {
            return context.getString(R.string.error_connection_failed);
        } else {
            return context.getString(R.string.error_connection_generic, t.getMessage());
        }
    }
}


