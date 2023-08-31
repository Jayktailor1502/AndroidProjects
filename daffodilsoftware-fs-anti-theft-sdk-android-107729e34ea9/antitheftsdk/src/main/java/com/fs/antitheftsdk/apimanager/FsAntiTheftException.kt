package com.fs.antitheftsdk.apimanager

import com.fs.antitheftsdk.base.Constants
import com.fs.antitheftsdk.constants.FsServiceErrorUtils

/*Api exception model class*/
internal class FsAntiTheftException : Throwable {

    private var mMessage: String? = null
    var fsServiceError:  FsServiceErrorUtils.FSServiceError? = null

    constructor(message: String?) {
        this.mMessage = message

        if(message?.contains(Constants.ALREADY_EXISTS) == true) {
            this.fsServiceError = FsServiceErrorUtils.FSServiceError.already_exists

        }
        else {
            this.fsServiceError = FsServiceErrorUtils.FSServiceError.internal_server_error
        }
    }

    constructor(message: String?, mErrorCode: Int) {
        this.fsServiceError = FsServiceErrorUtils().getEnumFromString(mErrorCode)
        this.mMessage = message
    }

    fun getmessage(): String? {
        return mMessage!!
    }

    fun setMessage(message: String?) {
        this.mMessage = message
    }
}