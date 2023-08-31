package com.fs.antitheftsdk.login.callbacks;

import com.fs.antitheftsdk.apimanager.FsAntiTheftException;
import com.fs.antitheftsdk.login.model.AuthenticationResponse;
import com.fs.antitheftsdk.login.model.BaseResponse;
import com.fs.antitheftsdk.network.exception.ApiResponse;

public interface IAuthenticationCallback {

    /**
     * This method provides the callback to Application AuthenticationResponse model
     * if authentication fails.
     */
    void onAuthenticationFail(FsAntiTheftException response);

    /**
     * This method provides the callback to Application AuthenticationResponse model
     * if authentication is successful.
     */
    void onAuthenticationSuccess(AuthenticationResponse response) throws ApiResponse;

    void onTokenSuccess(BaseResponse response) throws ApiResponse;
}
