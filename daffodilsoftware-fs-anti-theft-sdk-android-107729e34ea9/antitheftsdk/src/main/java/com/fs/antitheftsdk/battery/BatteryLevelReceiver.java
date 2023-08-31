package com.fs.antitheftsdk.battery;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.permissions.PermissionsUtil;

public class BatteryLevelReceiver extends BroadcastReceiver {

    PowerManager pm;
    PowerManager.WakeLock wl;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(PermissionsUtil.checkadminPermission(context)&& PermissionsUtil.checkDisplayOverlayPermission(context)) {
            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && (myKM.isDeviceLocked() || !isScreenOn)) {
                wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getString(R.string.wakelock_tag));
                wl.acquire(20 * 60 * 1000L /*20 minutes*/);
            }
            WorkManager mWorkManager = WorkManager.getInstance(context);
            mWorkManager.enqueue(OneTimeWorkRequest.from(BatteryWorker.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && (myKM.isDeviceLocked() || !isScreenOn)) {
                wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getString(R.string.wakelock_tag));
                wl.acquire(20 * 60 * 1000L /*20 minutes*/);
            }
        }
    }
}
