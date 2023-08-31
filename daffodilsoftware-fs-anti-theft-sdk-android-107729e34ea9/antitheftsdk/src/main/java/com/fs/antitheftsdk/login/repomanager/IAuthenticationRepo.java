package com.fs.antitheftsdk.login.repomanager;


import com.fs.antitheftsdk.login.model.AuthenticationRequest;
import com.fs.antitheftsdk.login.model.AuthenticationResponse;
import com.fs.antitheftsdk.login.model.BaseResponse;
import com.fs.antitheftsdk.login.model.TokenRequest;

import io.reactivex.Observable;
import retrofit2.Call;

public interface IAuthenticationRepo {

    Observable<AuthenticationResponse> login(AuthenticationRequest loginReq);

    Observable<BaseResponse> saveToken(TokenRequest tokenReq);

    Call<Void> generateToken(String userId);


}
