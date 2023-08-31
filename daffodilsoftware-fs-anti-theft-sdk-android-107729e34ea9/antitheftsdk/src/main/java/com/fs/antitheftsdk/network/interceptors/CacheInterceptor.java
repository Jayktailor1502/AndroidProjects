package com.fs.antitheftsdk.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This Interceptor adds the cache to request
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //if ApplyResponseCache set to true in api header this will enable caching for that api.
        if (!Boolean.valueOf(request.header("ApplyResponseCache"))) {
            Request builder = request.newBuilder().addHeader("Cache-Control", "no-cache")
                    .removeHeader("ApplyResponseCache").build();
            return chain.proceed(builder);
        }
        return chain.proceed(request);
    }
}
