package com.fs.antitheftsdk.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenRequest {

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
