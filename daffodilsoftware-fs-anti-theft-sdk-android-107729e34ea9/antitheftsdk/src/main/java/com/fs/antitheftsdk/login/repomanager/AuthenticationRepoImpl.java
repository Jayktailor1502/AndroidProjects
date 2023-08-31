package com.fs.antitheftsdk.login.repomanager;

import com.fs.antitheftsdk.login.model.AuthenticationRequest;
import com.fs.antitheftsdk.login.model.AuthenticationResponse;
import com.fs.antitheftsdk.login.model.BaseResponse;
import com.fs.antitheftsdk.login.model.TokenRequest;
import com.fs.antitheftsdk.network.Rx.ApiErrorHelper;
import com.fs.antitheftsdk.network.Rx.AppRxSchedulers;
import com.fs.antitheftsdk.network.Rx.RetryAPI;
import com.fs.antitheftsdk.network.network.NetworkManager;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import io.reactivex.Observable;
import retrofit2.Call;

public class AuthenticationRepoImpl implements IAuthenticationRepo {

    public Observable<AuthenticationResponse> login(AuthenticationRequest loginReq) {


        return NetworkManager.getApiClient().authenticate(loginReq, AppPreferences.getInstance().getString(AppPreferenceKey.API_KEY))
                .compose(new AppRxSchedulers().applySchedulers()).retryWhen(RetryAPI.retryWithExponentialBackOff())
                .onErrorResumeNext(new ApiErrorHelper<AuthenticationResponse>().getErrorParser());
    }

    @Override
    public Observable<BaseResponse> saveToken(TokenRequest tokenReq) {
        return NetworkManager.getApiClient().saveToken(tokenReq, AppPreferences.getInstance().getString(AppPreferenceKey.API_KEY))
                .compose(new AppRxSchedulers().applySchedulers()).retryWhen(RetryAPI.retryWithExponentialBackOff())
                .onErrorResumeNext(new ApiErrorHelper<BaseResponse>().getErrorParser());

    }

    @Override
    public Call<Void> generateToken(String userId) {
        return NetworkManager.getApiClient().generateToken(userId, AppPreferences.getInstance().getString(AppPreferenceKey.API_KEY),""+ AppPreferences.getInstance().getString("authToken"));
    }

}
