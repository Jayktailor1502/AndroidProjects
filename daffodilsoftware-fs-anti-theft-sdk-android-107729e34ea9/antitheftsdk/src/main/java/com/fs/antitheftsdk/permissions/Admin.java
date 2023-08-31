package com.fs.antitheftsdk.permissions;

import android.app.KeyguardManager;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import com.fs.antitheftsdk.activity.BlankActivity;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.camera.GetBackCoreService;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;
import com.fs.antitheftsdk.remote_lock.LockActivity;


public class Admin extends DeviceAdminReceiver {

    public static int numberOfAttemps;
    PowerManager pm;
    PowerManager.WakeLock wl;

    @Override
    public void onEnabled(Context ctxt, Intent intent) {
        onPasswordChanged(ctxt, intent);
    }

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
        ComponentName name = new ComponentName(ctxt, Admin.class);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager.hasGrantedPolicy(name, DeviceAdminInfo.USES_POLICY_WATCH_LOGIN)) {
            int no = devicePolicyManager.getCurrentFailedPasswordAttempts();
            if (no >= 3) {
                numberOfAttemps = no;
                triggerCamera(ctxt);
            }
        }
    }


    @Override
    public void onPasswordSucceeded(Context ctxt, Intent intent) {
        numberOfAttemps = 0;
        if(AppPreferences.getInstance()!=null) {
            if (AppPreferences.getInstance().getBoolean(AppPreferenceKey.IS_LOCKED_STATUS)) {
                Intent i = new Intent(ctxt, LockActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctxt.startActivity(i);
            }
        }
    }

    public void triggerCamera(Context context) {
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && (myKM.isDeviceLocked() || !isScreenOn)) {
                wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "sampleapp: mywakelocktag");
                wl.acquire(20 * 60 * 1000L);
        }

        if (Admin.numberOfAttemps >= 3) {
            if (Build.VERSION.SDK_INT > 29) {

                Constants.TriggerStatus=true;
                Intent i = new Intent(context, BlankActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            } else if (Build.VERSION.SDK_INT > 25) {
                context.startForegroundService(new Intent(context, GetBackCoreService.class));

            } else {
                context.startService(new Intent(context, GetBackCoreService.class));
            }

        }
    }
}
