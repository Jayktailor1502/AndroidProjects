package com.fs.antitheftsdk.sdkClient

internal interface IFsAntiTheftSdkClient {

    fun connect(baseUrl: String, versionName: String)

    fun clearPreferences()

    fun isConnected(): Boolean
}