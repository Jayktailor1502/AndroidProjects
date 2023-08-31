package com.fs.antitheftsdk.firebase;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.activity.BlankActivity;
import com.fs.antitheftsdk.activity.LockScreenActivity;
import com.fs.antitheftsdk.alarm.AlarmThread;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.battery.BatteryWorker;
import com.fs.antitheftsdk.camera.GetBackCoreService;
import com.fs.antitheftsdk.location.LocationThread;
import com.fs.antitheftsdk.permissions.PermissionsUtil;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;
import com.fs.antitheftsdk.remote_lock.LockActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import static com.fs.antitheftsdk.base.Constants.TAG;
import static com.fs.antitheftsdk.base.Constants.TriggerStatus;
import static com.fs.antitheftsdk.base.Constants.isLock;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    PowerManager pm;
    PowerManager.WakeLock wl;
    String TAG = "AntitheftSDK";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.e(TAG, getString(R.string.refreshedToken) + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String action = remoteMessage.getData().get(Constants.ACTION);
        String enc_key=remoteMessage.getData().get("password");
        Log.d(TAG,"msg+ "+remoteMessage.toString() + "\n action: " + action);
        if (Objects.equals(action, Constants.ALARM)) {
            new AlarmThread(this).start();


        } else if (Objects.equals(action, Constants.LOCATION)) {
            if (Boolean.TRUE.equals(PermissionsUtil.checkLocationPermission(this))) {
                new LocationThread(this).start();
                WorkManager mWorkManager = WorkManager.getInstance(this);
                mWorkManager.enqueue(OneTimeWorkRequest.from(BatteryWorker.class));
            }


        } else if (Objects.equals(action, Constants.CAMERA)) {

            if (Boolean.TRUE.equals(PermissionsUtil.checkCameraPermission(this))) {
                if (Build.VERSION.SDK_INT > 29) {
                    pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                    boolean isScreenOn = pm.isScreenOn();
                    KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
                    if (myKM.isDeviceLocked() || !isScreenOn) {
                        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getString(R.string.wakelock_tag));
                        wl.acquire(20 * 60 * 1000L /*20 minutes*/);

                    }

                    TriggerStatus=true;
                    Intent i = new Intent(this, BlankActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else if (Build.VERSION.SDK_INT > 25) {
                    startForegroundService(new Intent(this, GetBackCoreService.class));
                } else {
                    startService(new Intent(this, GetBackCoreService.class));
                }
            }


        } else if (Objects.equals(action, Constants.ALARM_OFF)) {
            Intent broadcastIntent = new Intent(Constants.BROADCAST_ALARM);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        } else if (Objects.equals(action, Constants.REMOTE_LOCKING)) {
            if (PermissionsUtil.checkadminPermission(this) && PermissionsUtil.checkDisplayOverlayPermission(this)) {

                isLock = true;
                pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && (myKM.isDeviceLocked() || !isScreenOn)) {
                    wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getString(R.string.wakelock_tag));
                    wl.acquire(20 * 60 * 1000L /*20 minutes*/);
                }
                WorkManager mWorkManager = WorkManager.getInstance(this);
                mWorkManager.enqueue(OneTimeWorkRequest.from(BatteryWorker.class));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && (myKM.isDeviceLocked() || !isScreenOn)) {
                    wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getString(R.string.wakelock_tag));
                    wl.acquire(20 * 60 * 1000L /*20 minutes*/);
                }
            }
            if (PermissionsUtil.checkLocationPermission(this)) {
                new LocationThread(this).start();
            }
            AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOCKED_STATUS,true);
            if(enc_key!=null) {
                if (!enc_key.isEmpty() || !enc_key.equals("")) {
                    AppPreferences.getInstance().putString(AppPreferenceKey.ENCRYPTED_KEY, enc_key);
                }
            }
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(AppPreferences.getInstance().getString(AppPreferenceKey.APPLICATION_PACKAGE_NAME));
            if (launchIntent != null) {
                startActivity(launchIntent);//null pointer check in case package name was not found
            }
            Intent i=new Intent(this,LockActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        } else if (Objects.equals(action, Constants.REMOTE_WIPE)) {
            if (PermissionsUtil.checkadminPermission(this) && PermissionsUtil.checkDisplayOverlayPermission(this)) {

                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
                devicePolicyManager.wipeData(0);
            }

        } else if (Objects.equals(action, Constants.REMOTE_UNLOCKING)) {
            if (PermissionsUtil.checkadminPermission(this) && PermissionsUtil.checkDisplayOverlayPermission(this)) {

                pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (myKM.isDeviceLocked()) {
                        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getString(R.string.wakelock_tag));
                        wl.acquire(20 * 60 * 1000L);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        Intent i = new Intent(this, LockScreenActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else if (!isScreenOn) {
                        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getString(R.string.wakelock_tag));
                        wl.acquire(20 * 60 * 1000L);
                    }
                }
            }

            AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOCKED_STATUS,false);

            Intent i=new Intent(this,LockActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }


    }


}