package com.fs.antitheftsdk.network.network;

import android.content.Context;

import com.fs.antitheftsdk.network.apis.Api;
import com.fs.antitheftsdk.network.interceptors.AuthorizationInterceptor;
import com.fs.antitheftsdk.network.interceptors.BasicHeaderInterceptor;
import com.fs.antitheftsdk.network.interceptors.CacheInterceptor;
import com.fs.antitheftsdk.network.interceptors.ConnectivityInterceptor;
import com.fs.antitheftsdk.network.interceptors.ResponseInterceptor;
import com.fs.antitheftsdk.network.interceptors.TimeoutInterceptor;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private static OkHttpClient sOkHttpClient;
    private static Api mApi;
    private static Retrofit retrofit;

    private NetworkManager() {
    }

    /**
     * Initial setup for network manager
     *
     * @param ctx App context, Needed for cache initialization
     */
    public static void init(Context ctx) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(ctx.getCacheDir(), NetworkConstants.CACHE_SIZE))
                .addInterceptor(new ConnectivityInterceptor(ctx))
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new TimeoutInterceptor())
                .addInterceptor(new BasicHeaderInterceptor())
                .addInterceptor(new AuthorizationInterceptor())
                .addNetworkInterceptor(new ResponseInterceptor())
                .connectTimeout(NetworkConstants.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(NetworkConstants.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        sOkHttpClient = builder.build();
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setLenient().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(NetworkManagerClient.getInstance().getBaseUrl())
                    .client(sOkHttpClient)
                    .build();
        return retrofit;
    }

    /**
     * @return {@link Api} Retrofit client
     */
    public static Api getApiClient() {
        if (mApi == null) {
            mApi = getRetrofitInstance().create(Api.class);
        }
        return mApi;
    }


}