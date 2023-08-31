package com.fs.antitheftsdk.camera;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.fs.antitheftsdk.R;

import static com.fs.antitheftsdk.base.Constants.TAG;


/*This class is the service to run the camera trigger functionality */
public class GetBackCoreService extends Service implements IFrontCaptureCallback {

    private SharedPreferences preferences;
    private static final com.fs.antitheftsdk.camera.GetBackStateFlags stateFlags = new com.fs.antitheftsdk.camera.GetBackStateFlags();

    public GetBackCoreService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= 26) {
            if (Build.VERSION.SDK_INT > 26) {
                String channelOneId = "com.fs.antitheftsdk";
                String channelOneName = "hiddencamera";
                NotificationChannel notificationChannel = null;
                notificationChannel = new NotificationChannel(channelOneId, channelOneName, NotificationManager.IMPORTANCE_MIN);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setShowBadge(true);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.createNotificationChannel(notificationChannel);
                }

                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setChannelId(channelOneId)
                        .setContentTitle(getString(R.string.service))
                        .setContentText(getString(R.string.capture_picture_using_foreground_service))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(icon)
                        .build();

                Intent notificationIntent = new Intent(getApplicationContext(), GetBackCoreService.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
                } else {
                    notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                }

                startForeground(104, notification);
            } else {
                startForeground(104, updateNotification());
            }
        } else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.service))
                    .setContentText(getString(R.string.capture_picture_using_foreground_service))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setOngoing(true).build();
            startForeground(104, notification);
        }

        stopSelf();
        takeAction();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        com.fs.antitheftsdk.camera.Utils.LogUtil.logD(Constants.LOG_TAG, getString(R.string.serviceDestroyed));
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPhotoCaptured(String filePath) {
        synchronized (stateFlags) {

            Toast.makeText(this, R.string.onPhotoCaptured, Toast.LENGTH_LONG).show();
            stateFlags.isPhotoCaptured = true;
            addBooleanPreference(Constants.PREFERENCE_IS_PHOTO_CAPTURED, stateFlags.isPhotoCaptured);
            com.fs.antitheftsdk.camera.Utils.LogUtil.logD(Constants.LOG_TAG, getString(R.string.image_Saved_at) + filePath);
            String photoPath = filePath;
            addStringPreference(Constants.PREFERENCE_PHOTO_PATH, photoPath);
        }
        takeAction();
    }

    private void addBooleanPreference(String key, boolean value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    private void addStringPreference(String key, String value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    private synchronized void takeAction() {
        capturePhoto();
    }

    private void capturePhoto() {
        com.fs.antitheftsdk.camera.Utils.LogUtil.logD(Constants.LOG_TAG, getString(R.string.inside_capture_thread));
        com.fs.antitheftsdk.camera.CameraView frontCapture = new com.fs.antitheftsdk.camera.CameraView(GetBackCoreService.this.getBaseContext());
        frontCapture.capturePhoto(GetBackCoreService.this);
    }

    @Override
    public void onCaptureError(int errorCode) {
        Log.e(TAG, String.valueOf(errorCode));
    }

    private Notification updateNotification() {
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT < 31) {
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, GetBackCoreService.class), 0);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, GetBackCoreService.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return new NotificationCompat.Builder(this)
                .setTicker("Ticker")
                .setContentTitle(this.getString(R.string.service))
                .setContentText(this.getString(R.string.capture_picture_using_foreground_service))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
    }
}