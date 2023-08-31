package com.fs.antitheftsdk.application;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.background.UploadManager;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.login.workmanager.LoginWorker;
import com.fs.antitheftsdk.network.network.NetworkManagerClient;
import com.fs.antitheftsdk.permissions.PermissionActivity;
import com.fs.antitheftsdk.permissions.PermissionsUtil;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;
import com.fs.antitheftsdk.remote_lock.LockActivity;
import com.fs.antitheftsdk.utils.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.fs.antitheftsdk.base.Constants.FCM_ERROR;
import static com.fs.antitheftsdk.base.Constants.TAG;

public class FsAntiTheft {

    static FsAntiTheft mantiTheft;

    /**
     * This function is used to initialize the object of FSAntiTheft class,
     * Firebase App and NetworkManagerClient
     */
    public static void init(Context context, String url, String versionname,String ApiKey,String appPackageName) {
        if (mantiTheft == null) {
            mantiTheft = new FsAntiTheft(context);
        }
        FirebaseApp.initializeApp(context);
        retreiveToken(context);
        NetworkManagerClient.getInstance().init(context, url, versionname);
        AppPreferences.getInstance().putString(AppPreferenceKey.API_KEY,ApiKey);
        AppPreferences.getInstance().putString(AppPreferenceKey.APPLICATION_PACKAGE_NAME,appPackageName);


    }


    /**
     * This function is used to return the object of this class
     */
    public static FsAntiTheft getInstance(Context context) {
        if (mantiTheft == null) {
            mantiTheft = new FsAntiTheft(context);
        }
        return mantiTheft;
    }


    /**
     * This is a private constructor which we used to initialize the AppPreferences object.
     */
    private FsAntiTheft(Context context) {
        AppPreferences.init(context);
    }


    /**
     * This function is used to check if the user is already logged in or not.
     * If it is not logged in then we start the activity.
     */

    public void performLoginToSdk(Context context, String email, String mobile, String applicationId,String emei) {
        if (AppPreferences.getInstance() != null && !AppPreferences.getInstance().isUserAlreadyLoggedIn()) {
            AppPreferences.getInstance().putString(AppPreferenceKey.EMEI,emei);
            Data input = new Data.Builder()
                    .putString(Constants.EMAIL, email)
                    .putString(Constants.MOBILE, mobile)
                    .putString(Constants.APPLICATION_ID, applicationId).build();
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(LoginWorker.class)
                    .setInputData(input)
                    .build();

            WorkManager.getInstance(context).enqueueUniqueWork("Login Worker", ExistingWorkPolicy.REPLACE, workRequest);

        }
        if (AppPreferences.getInstance() != null && AppPreferences.getInstance().isUserAlreadyLoggedIn()) {
            Log.d(context.getString(R.string.device_id), AppPreferences.getInstance().getString(AppPreferenceKey.DEVICE_ID));
            Log.d(context.getString(R.string.user_id), String.valueOf(AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID)));
            String s = AppPreferences.getInstance().getString(context.getString(R.string.authToken));
            Log.d(context.getString(R.string.authToken), s);
        }

    }

  /* This function is use to retreive the firebase token */
    public static void retreiveToken(Context context) {
        if (Utils.isNetworkConnected(context)) {

            FirebaseMessaging messageApp = FirebaseMessaging.getInstance();
            messageApp
                    .getToken()
                    .addOnFailureListener(ex -> Log.w(TAG, FCM_ERROR, ex))
                    .addOnSuccessListener(instanceIdResult -> {
                                String token = instanceIdResult;
                                AppPreferences.getInstance().putString(AppPreferenceKey.FIREBASE_TOKEN, token);
                                if (token != null) {
                                    UploadManager.sendToken(context);
                                }
                            });
        } else {
            Toast.makeText(context, context.getString(R.string.network_error_msg), Toast.LENGTH_LONG).show();
        }
    }

}
