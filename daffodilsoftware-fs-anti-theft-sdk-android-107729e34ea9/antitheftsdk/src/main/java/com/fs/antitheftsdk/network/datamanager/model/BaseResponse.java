package com.fs.antitheftsdk.network.datamanager.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class BaseResponse {
    @SerializedName("code")
    private int mCode;

    @SerializedName("messageCode")
    private String mMessageCode;

    @SerializedName("timezone")
    private String timeZone;

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public String getMessageCode() {
        return mMessageCode;
    }

    public void setMessageCode(String messageCode) {
        this.mMessageCode = messageCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
