package com.fs.antitheftsdk.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.fs.antitheftsdk.R
import com.fs.antitheftsdk.apimanager.FsAntiTheftException
import com.fs.antitheftsdk.base.Constants
import com.fs.antitheftsdk.base.Constants.TAG
import com.fs.antitheftsdk.base.Constants.isLock
import com.fs.antitheftsdk.battery.BatteryWorker
import com.fs.antitheftsdk.location.callback.ILocationCallback
import com.fs.antitheftsdk.login.callbacks.IAuthenticationCallback
import com.fs.antitheftsdk.login.callbacks.IDeviceRegisteredCallback
import com.fs.antitheftsdk.login.manager.AuthenticationManager
import com.fs.antitheftsdk.login.model.AuthenticationResponse
import com.fs.antitheftsdk.login.model.BaseResponse
import com.fs.antitheftsdk.login.model.DeviceRegisteredResponse
import com.fs.antitheftsdk.login.model.TokenRequest
import com.fs.antitheftsdk.network.exception.ApiResponse
import com.fs.antitheftsdk.permissions.PermissionsUtil
import com.fs.antitheftsdk.prefs.AppPreferenceKey
import com.fs.antitheftsdk.prefs.AppPreferences
import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient
import com.fs.antitheftsdk.utils.Utils

/* This class Handles the location trigger scenarios*/
internal object UploadManager {

    var latitude: String? = null
    var longitude: String? = null
    var timeStamp: String? = null
    var batteryLevel: Int = 0

    /*
    * This method is use to get the device location and hit api
    * */
    fun getLocation(location: Location, ctx: Context) {
        latitude = location.latitude.toString()
        longitude = location.longitude.toString()
        timeStamp = Utils.getTime()
        batteryLevel = Utils.getBatteryLevel(ctx)

        if (latitude?.isNotEmpty() == true && longitude?.isNotEmpty() == true && timeStamp?.isNotEmpty() == true) {
            val locationRequest = com.fs.antitheftsdk.location.model.LocationRequest()


            if(AppPreferences.getInstance()!=null)
            {
            if (AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID) != 0) {
                locationRequest.userId =
                    AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID)
                Log.v(
                    ctx.getString(R.string.user_id),
                    "" + AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID)
                )
            }}
            locationRequest.latitude = latitude
            locationRequest.longitude = longitude
            locationRequest.battery = batteryLevel
            locationRequest.locationOn = timeStamp


            FsAntiTheftClient.getInstance(ctx)
                ?.sendLocation(locationRequest, object : ILocationCallback {
                    override fun onLocationFail(response: FsAntiTheftException?) {
                        val handler = Handler(Looper.getMainLooper())
                        handler.post(Runnable {
                            Toast.makeText(ctx, response?.message.toString(), Toast.LENGTH_LONG)
                                .show()
                        })
                    }

                    override fun onLocationSuccess(response: BaseResponse) {
                        val handler = Handler(Looper.getMainLooper())
                        if (isLock) {
                            val devicePolicyManager =
                                ctx.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                            devicePolicyManager.lockNow()
                            isLock = false
                        }
                    }

                }, ctx)
        }
    }

    @JvmStatic
    fun sendToken(ctx: Context) {
        if (AppPreferences.getInstance() != null) {
            val request = TokenRequest()
            request.deviceId =
                Settings.System.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            Log.d(
                ctx.getString(R.string.device_id),
                Settings.System.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
            )
            request.deviceToken =
                AppPreferences.getInstance().getString(AppPreferenceKey.FIREBASE_TOKEN)
            Log.d(
                ctx.getString(R.string.token),
                AppPreferences.getInstance().getString(AppPreferenceKey.FIREBASE_TOKEN)
            )

            try {
                AuthenticationManager.getInstance()
                    .saveFirebaseToken(request, object : IAuthenticationCallback {
                        override fun onAuthenticationFail(response: FsAntiTheftException) {
                            Log.d(TAG,ctx.getString(R.string.authentication_failed))
                        }
                        @Throws(ApiResponse::class)
                        override fun onAuthenticationSuccess(response: AuthenticationResponse) {
                            Log.d(TAG,ctx.getString(R.string.authentication_failed))
                        }

                        @Throws(ApiResponse::class)
                        override fun onTokenSuccess(response: BaseResponse) {
                            isDeviceRegistered(ctx)
                        }
                    })
            } catch (e: ApiResponse) {
                Log.e(TAG,e.toString())
            }
        }
    }

    fun isDeviceRegistered(ctx:Context){
        AuthenticationManager.getInstance().checkDeviceRegister(ctx,object : IDeviceRegisteredCallback {
            override fun onRegisterFail(response: FsAntiTheftException?) {
                Log.d(TAG,ctx.getString(R.string.registration_failed))
            }

                override fun onRegisterSuccess(response: DeviceRegisteredResponse?) {
                    var result = response?.deviceRegister
                    var userId = response?.userId

                    if (userId != null) {
                        AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOGGED_IN, true)
                        AppPreferences.getInstance().putInt(AppPreferenceKey.USER_ID, userId)

                    }

                if (result == true){
                    val mWorkManager = WorkManager.getInstance(ctx)
                    mWorkManager.enqueue(OneTimeWorkRequest.from(BatteryWorker::class.java))

                }
                val broadcastIntent = Intent(Constants.BROADCAST_LOGIN)
                broadcastIntent.putExtra(Constants.REQUEST_EXTRA_LOGIN, result)
                LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcastIntent)


            }
        })
    }


}