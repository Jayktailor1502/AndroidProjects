package com.fs.antitheftsdk.network.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse extends Throwable {

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.data = new ErrorData(message);
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private transient ErrorData data;
    @SerializedName("debug")
    @Expose
    private String debug;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ErrorData getData() {
        return data;
    }

    public void setData(ErrorData data) {
        this.data = data;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}