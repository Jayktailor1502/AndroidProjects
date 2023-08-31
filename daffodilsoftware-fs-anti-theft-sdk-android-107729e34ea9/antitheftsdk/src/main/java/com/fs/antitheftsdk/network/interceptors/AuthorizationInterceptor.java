package com.fs.antitheftsdk.network.interceptors;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * User creds interceptor
 * <p>
 * Adds the user name, pass, apkVersion etc to API's having userCreds value set to true
 */
public class AuthorizationInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = null;
        String authToken = originalRequest.headers().get("Authorization");
        if (authToken != null) {
            newRequest = originalRequest.newBuilder()
                    .header("Authorization", authToken)
                    .build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
