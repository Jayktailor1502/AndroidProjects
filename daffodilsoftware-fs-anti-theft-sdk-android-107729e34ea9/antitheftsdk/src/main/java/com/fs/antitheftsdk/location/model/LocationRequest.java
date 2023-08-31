package com.fs.antitheftsdk.location.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/* This class is Request for Location api*/
public class LocationRequest {

    @SerializedName("userId")
    @Expose
    private Integer userId;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("battery")
    @Expose
    private Integer battery;

    @SerializedName("locationOn")
    @Expose
    private String locationOn;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getLocationOn() {
        return locationOn;
    }

    public void setLocationOn(String locationOn) {
        this.locationOn = locationOn;
    }
}
