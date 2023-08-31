package com.fs.antitheftsdk.login.callbacks

internal interface ISaveTokenCallback {
    /**
     * This method provides the callback to client to store the token
     * if authentication is successful.
     */
    fun onTokenReceive(token: String?, ref: String?, deviceref: String?)
}