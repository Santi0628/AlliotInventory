package com.alliot.inventory.network;

import androidx.annotation.NonNull;

import com.alliot.inventory.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", BuildConfig.API_KEY)
                .header("X-API-SECRET", BuildConfig.API_SECRET)
                .method(original.method(), original.body());

        return chain.proceed(builder.build());
    }
}
