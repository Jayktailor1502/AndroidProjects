package com.fs.antitheftsdk.login.callbacks;

import com.fs.antitheftsdk.apimanager.FsAntiTheftException;
import com.fs.antitheftsdk.login.model.DeviceRegisteredResponse;
import com.fs.antitheftsdk.network.exception.ApiResponse;

/* This interface prove callback for register api success and failure in response */
public interface IDeviceRegisteredCallback {

    void onRegisterFail(FsAntiTheftException response);

    void onRegisterSuccess(DeviceRegisteredResponse response) throws ApiResponse;
}
