package com.fs.antitheftsdk.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fs.antitheftsdk.base.Constants
import com.fs.antitheftsdk.location.LocationThread
import com.fs.antitheftsdk.permissions.PermissionsUtil
import com.fs.antitheftsdk.utils.Utils


/*
This activity is used to trigger the battery worker and get battery percentage
 */
internal class BatteryWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    var mContext: Context? = null
    var level = 0

    init {
        mContext = context
    }

    override fun doWork(): Result {
        registerBatteryBroadcast()
        return Result.success()
    }

    /* To Register the receiver*/
    private fun registerBatteryBroadcast() {
        mContext?.registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        try {
            Thread.sleep(Constants.BATTERY_INTERVAL)
        } catch (e: InterruptedException) {
            Log.e(Constants.TAG,e.toString())
        }
    }


    /* Broadcase Receiver for battery operation */
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            level = Utils.getBatteryLevel(context)
            if (level == Constants.BATTERY_LEVEL_10 || level == Constants.BATTERY_LEVEL_5 || level == Constants.BATTERY_LEVEL_1) {
                if(PermissionsUtil.checkLocationPermission(context)) {
                    LocationThread(context).start()
                }
                    if (level == Constants.BATTERY_LEVEL_1) {
                    unregisterReciever()
                }
            }
        }
    }

    /* To unregister the receiver*/
    fun unregisterReciever() {
        mContext?.unregisterReceiver(broadcastReceiver)
    }
}