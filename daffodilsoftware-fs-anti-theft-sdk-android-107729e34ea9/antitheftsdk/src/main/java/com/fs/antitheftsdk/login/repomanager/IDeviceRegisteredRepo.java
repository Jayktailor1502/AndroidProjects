package com.fs.antitheftsdk.login.repomanager;

import com.fs.antitheftsdk.login.model.DeviceRegisteredResponse;

import io.reactivex.Observable;

public interface IDeviceRegisteredRepo {
    Observable<DeviceRegisteredResponse> checkDeviceRegister(String deviceId);
}
