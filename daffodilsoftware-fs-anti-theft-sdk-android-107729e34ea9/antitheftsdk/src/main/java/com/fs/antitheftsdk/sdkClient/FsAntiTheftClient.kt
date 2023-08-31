package com.fs.antitheftsdk.sdkClient

import android.annotation.SuppressLint
import android.content.Context
import com.fs.antitheftsdk.camera.api.callbacks.IImageUploadCallback
import com.fs.antitheftsdk.camera.api.model.GetApiUrlRequest
import com.fs.antitheftsdk.location.callback.ILocationCallback
import com.fs.antitheftsdk.location.model.LocationRequest
import com.fs.antitheftsdk.login.callbacks.IAuthenticationCallback
import com.fs.antitheftsdk.login.callbacks.ISaveTokenCallback
import com.fs.antitheftsdk.login.manager.AuthenticationManager
import com.fs.antitheftsdk.login.model.AuthenticationRequest
import com.fs.antitheftsdk.network.network.NetworkManagerClient
import com.fs.antitheftsdk.prefs.AppPreferenceKey
import com.fs.antitheftsdk.prefs.AppPreferences

internal class FsAntiTheftClient(private val mContext: Context) :
    IFsAntiTheftSdkClient, ISaveTokenCallback {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mFsAntiTheftClient: FsAntiTheftClient? = null

        @JvmStatic
        fun getInstance(context: Context): FsAntiTheftClient? {
            if (mFsAntiTheftClient == null) {
                mFsAntiTheftClient = FsAntiTheftClient(context)
            }
            return mFsAntiTheftClient
        }

        @JvmStatic
        fun getContext(): Context? {
            return mFsAntiTheftClient?.mContext
        }
    }


    override fun onTokenReceive(token: String?, ref: String?, deviceref: String?) {
        AppPreferences.getInstance().putString(AppPreferenceKey.TOKEN, token)
        AppPreferences.getInstance().putString(AppPreferenceKey.USER_REF, ref)
        AppPreferences.getInstance().putString(AppPreferenceKey.DEV_REF, deviceref)
    }

    fun logoutUser() {
        AppPreferences.getInstance().removePref()
    }

    override fun connect(baseUrl: String, versionName: String) {
        initNetworkManager(baseUrl, versionName)
    }

    private fun initNetworkManager(baseUrl: String, versionName: String) {
        NetworkManagerClient.getInstance().init(mContext, baseUrl, versionName)
    }


    override fun clearPreferences() {
        TODO("Not yet implemented")
    }

    override fun isConnected(): Boolean {
        TODO("Not yet implemented")
    }

    fun callAuthenticateSdk(
        authRequest: AuthenticationRequest,
        authCallback: IAuthenticationCallback,
        context: Context
    ) {
        AuthenticationManager.getInstance().callAuthenticateSdk(authRequest, authCallback, context)
    }

    fun sendLocation(locationRequest: LocationRequest, locationCallback: ILocationCallback, context: Context) {
        AuthenticationManager.getInstance().sendLocationRequest(locationRequest, locationCallback, context)
    }

    fun getImageUrl(apiUrlRequest: GetApiUrlRequest, updateurlCallback: IImageUploadCallback, context: Context) {

        AuthenticationManager.getInstance().getUploadImageUrl(apiUrlRequest, updateurlCallback, context)
    }

}