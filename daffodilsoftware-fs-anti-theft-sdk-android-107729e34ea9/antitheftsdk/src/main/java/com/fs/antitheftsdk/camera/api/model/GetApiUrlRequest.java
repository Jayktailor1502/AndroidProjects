package com.fs.antitheftsdk.camera.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetApiUrlRequest {
    @SerializedName("userId")
    @Expose
    private Integer userId;

    @SerializedName("imageType")
    @Expose
    String imageType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
