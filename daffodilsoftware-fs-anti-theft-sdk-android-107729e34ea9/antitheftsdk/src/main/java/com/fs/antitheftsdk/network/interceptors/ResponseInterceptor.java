package com.fs.antitheftsdk.network.interceptors;

import android.util.Log;

import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.prefs.AppPreferences;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor to cache data and maintain it specified time.
 * <p>
 * If the same network request is sent within specified time,
 * the response is retrieved from cache.
 */
public class ResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response originalResponse = chain.proceed(chain.request());
        String authToken = originalResponse.header("Authorization");
        if (authToken != null) {
            AppPreferences.getInstance().putString(Constants.AUTH_TOKEN, authToken);
            Log.d(Constants.AUTH_TOKEN, authToken);
        }
        if (Boolean.valueOf(request.header("ApplyResponseCache"))) {
            return originalResponse.newBuilder()
                    .removeHeader("ApplyResponseCache")
                    .header("Cache-Control", String.format(Locale.getDefault(), "max-age=%d, only-if-cached, max-stale=%d", 1200, 0))
                    .build();
        }

        return originalResponse;
    }
}