package com.alliot.inventory.network;

import android.content.Context;
import android.util.Log;

import com.alliot.inventory.R;
import com.alliot.inventory.repository.ErrorType;

public final class ErrorHandler {

    private static final String TAG = "ErrorHandler";

    private ErrorHandler() {}

    public static String toMessage(Context context, ErrorType errorType) {
        if (errorType == null) return context.getString(R.string.error_connection_failed);

        Log.w(TAG, "Error: " + errorType.name()
                + (errorType.getHttpCode() != 0 ? " (HTTP " + errorType.getHttpCode() + ")" : ""));

        switch (errorType) {
            case UNAUTHORIZED:       return context.getString(R.string.error_unauthorized);
            case FORBIDDEN:          return context.getString(R.string.error_forbidden);
            case NOT_FOUND:          return context.getString(R.string.error_not_found);
            case SERVER_ERROR:       return context.getString(R.string.error_server);
            case NO_INTERNET:        return context.getString(R.string.error_no_internet);
            case TIMEOUT:            return context.getString(R.string.error_timeout);
            case CONNECTION_FAILED:  return context.getString(R.string.error_connection_failed);
            default:
                if (errorType.getHttpCode() != 0) {
                    return context.getString(R.string.error_server_code, errorType.getHttpCode());
                }
                String raw = errorType.getRawMessage();
                return raw != null
                        ? context.getString(R.string.error_connection_generic, raw)
                        : context.getString(R.string.error_connection_failed);
        }
    }
}
