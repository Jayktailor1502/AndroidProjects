package com.fs.antitheftsdk.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceRegisteredResponse {
    @SerializedName("isDeviceRegister")
    @Expose
    private Boolean isDeviceRegister;

    @SerializedName("userId")
    @Expose
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getDeviceRegister() {
        return isDeviceRegister;
    }

    public void setDeviceRegister(Boolean deviceRegister) {
        isDeviceRegister = deviceRegister;
    }
}
