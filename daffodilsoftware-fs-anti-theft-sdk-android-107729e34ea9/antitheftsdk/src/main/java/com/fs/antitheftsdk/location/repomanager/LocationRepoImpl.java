package com.fs.antitheftsdk.location.repomanager;

import com.fs.antitheftsdk.location.model.LocationRequest;
import com.fs.antitheftsdk.login.model.BaseResponse;
import com.fs.antitheftsdk.network.Rx.ApiErrorHelper;
import com.fs.antitheftsdk.network.Rx.AppRxSchedulers;
import com.fs.antitheftsdk.network.Rx.RetryAPI;
import com.fs.antitheftsdk.network.network.NetworkManager;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import io.reactivex.Observable;

public class LocationRepoImpl implements LocationRepo{
    @Override
    public Observable<BaseResponse> sendLocation(LocationRequest requestObj) {
        return NetworkManager.getApiClient().sendLocation(requestObj, AppPreferences.getInstance().getString(AppPreferenceKey.API_KEY),""+ AppPreferences.getInstance().getString("authToken"))
                .compose(new AppRxSchedulers().applySchedulers()).retryWhen(RetryAPI.retryWithExponentialBackOff())
                .onErrorResumeNext(new ApiErrorHelper<BaseResponse>().getErrorParser());
    }
}
