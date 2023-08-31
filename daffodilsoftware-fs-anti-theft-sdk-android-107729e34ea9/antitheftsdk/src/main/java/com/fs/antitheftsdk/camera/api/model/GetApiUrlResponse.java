package com.fs.antitheftsdk.camera.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetApiUrlResponse {

    @SerializedName("uploadUrl")
    @Expose
    private String uploadUrl;


    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}
