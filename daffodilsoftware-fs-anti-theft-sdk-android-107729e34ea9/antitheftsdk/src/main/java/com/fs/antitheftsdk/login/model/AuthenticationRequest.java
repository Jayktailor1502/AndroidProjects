package com.fs.antitheftsdk.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class
AuthenticationRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("mDeviceMarketName")
    @Expose
    private String mDeviceMarketName;
    @SerializedName("mDeviceOSVersion")
    @Expose
    private String mDeviceOSVersion;
    @SerializedName("mDeviceOS")
    @Expose
    private String mDeviceOS;
    @SerializedName("mDeviceId")
    @Expose
    private String mDeviceId;
    @SerializedName("mDeviceModel")
    @Expose
    private String mDeviceModel;
    @SerializedName("mDeviceManufacturer")
    @Expose
    private String mDeviceManufacturer;
    @SerializedName("mDeviceTotalStorage")
    @Expose
    private String mDeviceTotalStorage;
    @SerializedName("mDeviceTotalRAM")
    @Expose
    private String mDeviceTotalRAM;
    @SerializedName("mDeviceImei")
    @Expose
    private String mDeviceImei;
    @SerializedName("applicationId")
    @Expose
    private String applicationId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
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

    public void setMobile(String msdidn) {
        this.mobile = msdidn;
    }

    public String getmDeviceMarketName() {
        return mDeviceMarketName;
    }

    public void setmDeviceMarketName(String mDeviceMarketName) {
        this.mDeviceMarketName = mDeviceMarketName;
    }

    public String getmDeviceOSVersion() {
        return mDeviceOSVersion;
    }

    public void setmDeviceOSVersion(String mDeviceOSVersion) {
        this.mDeviceOSVersion = mDeviceOSVersion;
    }

    public String getmDeviceOS() {
        return mDeviceOS;
    }

    public void setmDeviceOS(String mDeviceOS) {
        this.mDeviceOS = mDeviceOS;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public String getmDeviceModel() {
        return mDeviceModel;
    }

    public void setmDeviceModel(String mDeviceModel) {
        this.mDeviceModel = mDeviceModel;
    }

    public String getmDeviceManufacturer() {
        return mDeviceManufacturer;
    }

    public void setmDeviceManufacturer(String mDeviceManufacturer) {
        this.mDeviceManufacturer = mDeviceManufacturer;
    }

    public String getmDeviceTotalStorage() {
        return mDeviceTotalStorage;
    }

    public void setmDeviceTotalStorage(String mDeviceTotalStorage) {
        this.mDeviceTotalStorage = mDeviceTotalStorage;
    }

    public String getmDeviceTotalRAM() {
        return mDeviceTotalRAM;
    }

    public void setmDeviceTotalRAM(String mDeviceTotalRAM) {
        this.mDeviceTotalRAM = mDeviceTotalRAM;
    }

    public String getmDeviceImei() {
        return mDeviceImei;
    }

    public void setmDeviceImei(String mDeviceImei) {
        this.mDeviceImei = mDeviceImei;
    }
}
