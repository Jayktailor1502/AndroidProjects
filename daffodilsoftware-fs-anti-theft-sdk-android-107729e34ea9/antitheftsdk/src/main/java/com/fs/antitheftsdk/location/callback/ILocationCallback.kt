package com.fs.antitheftsdk.location.callback

import com.fs.antitheftsdk.apimanager.FsAntiTheftException
import com.fs.antitheftsdk.login.model.BaseResponse
import com.fs.antitheftsdk.network.exception.ApiResponse

internal interface ILocationCallback {
    /**
     * This method provides the callback to Application LocationResponse model
     * if authentication fails.
     */
    fun onLocationFail(response: FsAntiTheftException?)

    /**
     * This method provides the callback to Application LocationResponse model
     * if authentication is successful.
     */
    @Throws(ApiResponse::class)
    fun onLocationSuccess(response: BaseResponse)
}