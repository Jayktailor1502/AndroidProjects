package com.fs.antitheftsdk.camera.work_manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fs.antitheftsdk.base.Constants
import com.fs.antitheftsdk.base.Constants.TAG
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


internal class ImageUploadWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    var mContext: Context? = null
    var mUploadUrl: String? = null
    var file: File? = null
    lateinit var imageString: Uri

    init {
        mContext = context
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val images = inputData.getString(Constants.IMAGE_STRING)

        imageString = Uri.fromFile(File(images))
        mUploadUrl = inputData.getString(Constants.UPLOAD_URL)

        val iStream: InputStream? = mContext?.contentResolver?.openInputStream(imageString)
        val inputData = iStream?.let { getBytes(it) }

        val storageDir: File = mContext?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())


        try {
            file = File.createTempFile(timeStamp, ".png", storageDir)
        } catch (e: IOException) {
            Log.e(TAG,e.toString())
        }
        val fo = FileOutputStream(file)
        fo.write(inputData)
        fo.close()

        uploadImage()

        return Result.success()
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun uploadImage() {
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType? = "image/png".toMediaTypeOrNull()
        val body: RequestBody = file!!.asRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url(mUploadUrl!!)
            .method("PUT", body)
            .addHeader("Content-Type", "image/png")
            .build()
        val response: Response = client.newCall(request).execute()
        Log.d(TAG, response.toString())
        if (response.isSuccessful) {
            val broadcastIntent = Intent(Constants.BROADCAST_API)
            LocalBroadcastManager.getInstance(mContext!!).sendBroadcast(broadcastIntent)
        }
    }
}