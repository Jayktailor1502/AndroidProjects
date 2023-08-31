package com.fs.antitheftsdk.camera.work_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.*
import com.fs.antitheftsdk.R
import com.fs.antitheftsdk.apimanager.FsAntiTheftException
import com.fs.antitheftsdk.base.Constants
import com.fs.antitheftsdk.camera.ObjectSerializer
import com.fs.antitheftsdk.camera.api.callbacks.IImageUploadCallback
import com.fs.antitheftsdk.camera.api.model.GetApiUrlRequest
import com.fs.antitheftsdk.camera.api.model.GetApiUrlResponse
import com.fs.antitheftsdk.prefs.AppPreferenceKey
import com.fs.antitheftsdk.prefs.AppPreferences
import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient
import java.io.File


internal class CameraWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    var mContext: Context? = null
    var mUploadUrl: String? = null
    var file: File? = null
    var imagesList: java.util.ArrayList<String>? = null
    var imageType: String? = null
    var userid: Int = 0

    init {
        mContext = context
    }

    override fun doWork(): Result {
        userid = inputData.getInt(Constants.USER_ID, 1)
        imageType = inputData.getString(Constants.IMAGE_TYPE)

        val filterApi = IntentFilter(Constants.BROADCAST_API)
        LocalBroadcastManager.getInstance(mContext!!).registerReceiver(broadcastReceiver, filterApi)

        imagesList = ArrayList()
        imagesList = ObjectSerializer.deserialize(
            AppPreferences.getInstance().getString(AppPreferenceKey.URI)
        ) as java.util.ArrayList<String>?

        if (Constants.count == 4) {
            Constants.count = 0
        }

        if (imageType != null) {
            registerCameraApi(userid, imageType!!, imagesList!![0])
        }

        return Result.success()
    }

    private fun registerCameraApi(userid: Int, type: String, images: String) {
        var getApiUrlRequest = GetApiUrlRequest()
        getApiUrlRequest.userId = userid
        getApiUrlRequest.imageType = type

        val handler = Handler(Looper.getMainLooper())
        handler.post(Runnable {

            FsAntiTheftClient.getInstance(mContext!!)
                ?.getImageUrl(getApiUrlRequest, object : IImageUploadCallback {
                    override fun onGetUrlSuccess(response: GetApiUrlResponse?) {

                        if (response != null) {
                            Log.v(Constants.CAMERA, response.uploadUrl )
                            mUploadUrl = response.uploadUrl

                            val input = Data.Builder()
                                .putString(Constants.IMAGE_TYPE, Constants.IMAGE_TYPE_PNG)
                                .putString(Constants.UPLOAD_URL, mUploadUrl)
                                .putString(Constants.IMAGE_STRING, images)
                                .build()

                            val workRequest =
                                OneTimeWorkRequest.Builder(ImageUploadWorker::class.java)
                                    .setInputData(input)
                                    .build()
                            WorkManager.getInstance(mContext!!).enqueueUniqueWork(
                                Constants.CAMERA_MANAGER,
                                ExistingWorkPolicy.REPLACE,
                                workRequest
                            )
                        }
                    }

                    override fun onGetUrlFail(response: FsAntiTheftException?) {
                        if (response != null) {
                            Log.v(Constants.CAMERA, response.getmessage()!!)
                        }
                        Toast.makeText(
                            mContext,
                            mContext?.getString(R.string.failed_to_upload_image),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, mContext!!)
        })
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Constants.count < 4) {
                if (imageType != null) {

                    registerCameraApi(userid, imageType!!, imagesList!![Constants.count])
                }
                Constants.count++
                if (Constants.count == 4) {
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
                }
            }
        }
    }
}