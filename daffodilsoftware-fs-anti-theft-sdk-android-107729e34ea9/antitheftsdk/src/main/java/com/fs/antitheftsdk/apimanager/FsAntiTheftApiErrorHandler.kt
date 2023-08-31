package com.fs.antitheftsdk.apimanager

import android.content.Context
import com.fs.antitheftsdk.R
import com.fs.antitheftsdk.base.Constants
import com.fs.antitheftsdk.base.ErrorCodes
import com.fs.antitheftsdk.constants.FsServiceErrorUtils
import com.fs.antitheftsdk.network.exception.ApiResponse
import com.fs.antitheftsdk.network.exception.NoConnectivityException

/* This class Handles the api exceptions */
internal class FsAntiTheftApiErrorHandler(var throwable: Throwable, var context: Context?) {

    private var mContext: Context? = null
    var defaultMessage: String? = Constants.ERROR_OCCURED
    var code: Int = 0

    fun processError(): FsAntiTheftException? {
        this.mContext = context
        var errorResponse: ApiResponse? = null

        when (throwable) {

            is ApiResponse -> {
                errorResponse = throwable as ApiResponse
                defaultMessage = errorResponse.message
                errorResponse = ApiResponse(ErrorCodes.Error_Code_409, R.string.already_exist)
                code = 409

            }

            is NoConnectivityException -> {
                errorResponse = ApiResponse(ErrorCodes.errorCode598, R.string.network_error_msg)
                code = 598
            }

            else -> {
                errorResponse =
                    ApiResponse(ErrorCodes.errorCode400, R.string.technical_error_msg)
                code = 400

            }
        }

        return FsAntiTheftException(getStringMessage(code), code)
    }


    private fun getStringMessage(errorCode: Int): String? {
        if (mContext == null) return defaultMessage

        return when (errorCode) {
            FsServiceErrorUtils.FSServiceError.internal_server_error.code -> context?.getString(R.string.technical_error_msg)
            FsServiceErrorUtils.FSServiceError.already_exists.code -> context?.getString(R.string.already_exist)
            FsServiceErrorUtils.FSServiceError.invalid_input.code -> context?.getString(R.string.invalid_input)

            else -> context!!.getString(R.string.technical_error_msg)
        }
    }
}