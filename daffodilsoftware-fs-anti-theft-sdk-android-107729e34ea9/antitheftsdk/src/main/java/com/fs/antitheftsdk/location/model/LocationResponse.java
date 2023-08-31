package com.fs.antitheftsdk.location.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/* This class is Response for location api*/
public class LocationResponse {
    @SerializedName("isError")
    @Expose
    private Boolean isError;
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
