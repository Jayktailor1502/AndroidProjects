package com.fs.antitheftsdk.network.interceptors;

import android.content.Context;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.network.exception.NoConnectivityException;
import com.fs.antitheftsdk.network.utils.ConnectivityUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private final Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!ConnectivityUtils.isNetworkEnabled(mContext)) {
            throw new NoConnectivityException(mContext.getString(R.string.network_error_msg));
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

}