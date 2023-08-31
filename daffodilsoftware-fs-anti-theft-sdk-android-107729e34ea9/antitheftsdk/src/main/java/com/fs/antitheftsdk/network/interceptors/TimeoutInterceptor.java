package com.fs.antitheftsdk.network.interceptors;

import android.text.TextUtils;

import com.fs.antitheftsdk.base.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TimeoutInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        int connectTimeout = chain.connectTimeoutMillis();
        int readTimeout = chain.readTimeoutMillis();
        int writeTimeout = chain.writeTimeoutMillis();

        String connectNew = request.header(Constants.CONNECT_TIMEOUT);
        String readNew = request.header(Constants.READ_TIMEOUT);
        String writeNew = request.header(Constants.WRITE_TIMEOUT);

        if (!TextUtils.isEmpty(connectNew)) {
            connectTimeout = Integer.valueOf(connectNew);
        }
        if (!TextUtils.isEmpty(readNew)) {
            readTimeout = Integer.valueOf(readNew);
        }
        if (!TextUtils.isEmpty(writeNew)) {
            writeTimeout = Integer.valueOf(writeNew);
        }

        return chain
                .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                .withReadTimeout(readTimeout, TimeUnit.SECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
                .proceed(request);
    }
}


