package com.fs.antitheftsdk.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AuthenticationResponse {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("status")
    @Expose
    private String status;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
