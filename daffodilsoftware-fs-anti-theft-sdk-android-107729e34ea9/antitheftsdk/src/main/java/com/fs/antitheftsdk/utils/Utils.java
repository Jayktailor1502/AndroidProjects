package com.fs.antitheftsdk.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.fs.antitheftsdk.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.BATTERY_SERVICE;

public class Utils {
    private Utils() {
    }

    public static boolean isNetworkConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        return activeNW != null && activeNW.isConnected();
    }

    public static void showDialogError(Context context, String message, String title, String positive) {
        final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Base_Theme_AppCompat_Dialog))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive, (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(true)
                .create();
        alertDialog.show();
    }

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date());
    }

    public static int getBatteryLevel(Context ctx) {
        BatteryManager bm = (BatteryManager) ctx.getSystemService(BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }
}