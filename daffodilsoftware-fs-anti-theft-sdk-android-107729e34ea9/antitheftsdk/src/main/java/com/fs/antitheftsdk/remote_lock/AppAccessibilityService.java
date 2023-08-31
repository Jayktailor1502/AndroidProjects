
package com.fs.antitheftsdk.remote_lock;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import java.util.List;


public class AppAccessibilityService extends AccessibilityService {

    String TAG = "Accessbility";


    @Override
    public void onCreate() {
        super.onCreate();

    }

    private Boolean getKeyboardPkg(String pkg) {
        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        List<InputMethodInfo> list = imeManager.getEnabledInputMethodList();
        for (InputMethodInfo i : list) {
            if (i.getPackageName().equals(pkg) || i.getPackageName().contains(pkg)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        /**
         *  nothing to do
         */


        Boolean lockCheck = AppPreferences.getInstance().getBoolean(AppPreferenceKey.IS_LOCKED_STATUS);
        if (lockCheck) {
            final CharSequence packageName = accessibilityEvent.getPackageName();
            String flutterPkg = AppPreferences.getInstance().getString(AppPreferenceKey.APPLICATION_PACKAGE_NAME);
            if (packageName != null && packageName.length() > 0) {
                try {

                    if ("br.com.fs.pix.security".equals(packageName) ||
                            "android".equals(packageName) ||
                            getKeyboardPkg(packageName.toString()) ||
                            "com.android.systemui".equals(packageName) ||
                            "launcher3".contains(packageName) ||
                            flutterPkg.equals(packageName)) {

                    } else if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                        if (AppPreferences.getInstance() != null) {
                            if (AppPreferences.getInstance().getBoolean(AppPreferenceKey.IS_LOCKED_STATUS)) {
                                performGlobalAction(GLOBAL_ACTION_BACK);
                                performGlobalAction(GLOBAL_ACTION_BACK);
                            }
                        }
                    } else {
                        if (AppPreferences.getInstance() != null) {
                            if (AppPreferences.getInstance().getBoolean(AppPreferenceKey.IS_LOCKED_STATUS)) {

                                Intent i = new Intent(getApplicationContext(), LockActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(i);
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Catch: some Exeption Occoured");
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onInterrupt() {
        Log.v("tag", "intruppted");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }

}