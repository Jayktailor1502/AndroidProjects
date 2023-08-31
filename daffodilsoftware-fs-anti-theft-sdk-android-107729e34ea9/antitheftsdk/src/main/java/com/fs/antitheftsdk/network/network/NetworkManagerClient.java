package com.fs.antitheftsdk.network.network;

import android.content.Context;

/**
 * Singleton implementation for Network Manager Initialization
 */
public class NetworkManagerClient {
    private static NetworkManagerClient sInstance;
    private String mBaseUrl;
    private String mAppVersion;


    private NetworkManagerClient() {
    }

    public static NetworkManagerClient getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkManagerClient();
        }
        return sInstance;
    }

    public void init(Context context, String baseUrl, String appVersion) {
        mBaseUrl = baseUrl;
        this.mAppVersion = appVersion;
        NetworkManager.init(context);
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }
}
