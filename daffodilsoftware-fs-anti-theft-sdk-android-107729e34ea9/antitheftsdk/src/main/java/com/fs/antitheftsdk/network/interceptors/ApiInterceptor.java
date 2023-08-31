package com.fs.antitheftsdk.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Adds Auth headers to api's
 */
public class ApiInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build();

        return chain.proceed(newRequest);
    }
}
