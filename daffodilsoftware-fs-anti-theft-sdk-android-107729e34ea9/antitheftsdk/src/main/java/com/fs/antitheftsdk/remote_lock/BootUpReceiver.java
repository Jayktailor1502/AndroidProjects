package com.fs.antitheftsdk.remote_lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(AppPreferences.getInstance()!=null) {
            if (AppPreferences.getInstance().getBoolean(AppPreferenceKey.IS_LOCKED_STATUS)) {
                Intent i = new Intent(context, LockActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }

}
