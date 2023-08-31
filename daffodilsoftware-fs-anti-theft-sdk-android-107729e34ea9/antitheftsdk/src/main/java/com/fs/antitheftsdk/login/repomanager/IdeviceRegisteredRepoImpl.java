package com.fs.antitheftsdk.login.repomanager;

import com.fs.antitheftsdk.login.model.DeviceRegisteredResponse;
import com.fs.antitheftsdk.network.Rx.ApiErrorHelper;
import com.fs.antitheftsdk.network.Rx.AppRxSchedulers;
import com.fs.antitheftsdk.network.Rx.RetryAPI;
import com.fs.antitheftsdk.network.network.NetworkManager;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import io.reactivex.Observable;

public class IdeviceRegisteredRepoImpl implements IDeviceRegisteredRepo {
    public Observable<DeviceRegisteredResponse> checkDeviceRegister(String deviceId) {


        return NetworkManager.getApiClient().checkDeviceRegistered(deviceId, AppPreferences.getInstance().getString(AppPreferenceKey.API_KEY))
                .compose(new AppRxSchedulers().applySchedulers()).retryWhen(RetryAPI.retryWithExponentialBackOff())
                .onErrorResumeNext(new ApiErrorHelper<DeviceRegisteredResponse>().getErrorParser());
    }
}
