package com.fs.antitheftsdk.network.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorData {

    public ErrorData(String str) {
        this.errorDetails = str;
    }

    @SerializedName("error_type")
    @Expose
    private String errorType;
    @SerializedName("error_details")
    @Expose
    private String errorDetails;

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        if (errorDetails == null) errorDetails = "Some error occurred. Please try again.";

        this.errorDetails = errorDetails;
    }
}