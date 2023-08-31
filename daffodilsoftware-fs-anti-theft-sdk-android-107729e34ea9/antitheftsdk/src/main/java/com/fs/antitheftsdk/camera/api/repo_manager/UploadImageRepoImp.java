package com.fs.antitheftsdk.camera.api.repo_manager;

import com.fs.antitheftsdk.camera.api.model.GetApiUrlRequest;
import com.fs.antitheftsdk.camera.api.model.GetApiUrlResponse;
import com.fs.antitheftsdk.network.Rx.ApiErrorHelper;
import com.fs.antitheftsdk.network.Rx.AppRxSchedulers;
import com.fs.antitheftsdk.network.Rx.RetryAPI;
import com.fs.antitheftsdk.network.network.NetworkManager;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import io.reactivex.Observable;

public class UploadImageRepoImp implements IUploadImageRepo {

    @Override
    public Observable<GetApiUrlResponse> getImageUrl(GetApiUrlRequest loginReq) {
        return NetworkManager.getApiClient().getImageUrl(loginReq.getUserId(), loginReq.getImageType(), AppPreferences.getInstance().getString(AppPreferenceKey.API_KEY), "" + AppPreferences.getInstance().getString("authToken"))
                .compose(new AppRxSchedulers().applySchedulers()).retryWhen(RetryAPI.retryWithExponentialBackOff())
                .onErrorResumeNext(new ApiErrorHelper<GetApiUrlResponse>().getErrorParser());
    }
}














