package com.fs.antitheftsdk.remote_lock;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class LockActivity extends AppCompatActivity {

    static String enc_Key;
    Activity activity;
    Context mcontext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;

        mcontext=this;
    }


    @Override
    protected void onResume() {
        super.onResume();

        //hideSystemUI();
        //hideSystemBars();

        if(AppPreferences.getInstance().getBoolean(AppPreferenceKey.IS_LOCKED_STATUS))
        {
            try {
                enc_Key=decryption("b765674c-f235-11ec",AppPreferences.getInstance().getString(AppPreferenceKey.ENCRYPTED_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            startLockService();
            finish();
        }
        else
        {
            stopLockService();
            finish();
        }

    }

    public void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public String decryption(String seed, String encrypted) throws Exception {
        byte[] keyb = seed.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(keyb);
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES");
        dcipher.init(Cipher.DECRYPT_MODE, skey);

        byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
        return new String(clearbyte);
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        }
        return result;
    }
    private static class EncryptedData {
        public byte[] salt;
        public byte[] iv;
        public byte[] encryptedData;
    }
    public void startLockService()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {

                // Launch service right away - the user has already previously granted permission
                //new BlockStatusBar(this,true).collapseNow();
                launchMainService();
            }
            else {

                // Check that the user has granted permission, and prompt them if not
                checkDrawOverlayPermission();
            }
        }
    }

    public void stopLockService()
    {
        Intent svc = new Intent(this, RemoteLockerService.class);

        stopService(svc);
        RemoteLockerService.wrongpasscount=0;

        AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOCKED_STATUS,false);
    }

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    private void launchMainService() {

        Intent svc = new Intent(this, RemoteLockerService.class);
        svc.putExtra("key",enc_Key);

        stopService(svc);

        startService(svc);

        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
           ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.moveTaskToFront(getTaskId(), 0);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i("TAG", "onWindowFocusChanged()");
        try {
            if (!hasFocus) {
                Log.d("TAG", "close status bar attempt");
                //option 1
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
                Class<?> statusbarManager = Class
                        .forName("android.app.StatusBarManager");

                if (currentApiVersion <= 16) {
                    Method collapse = statusbarManager.getMethod("collapse");
                    collapse.setAccessible(true);
                    collapse.invoke(service);
                } else {
                    Method collapse = statusbarManager.getMethod("collapsePanels");
                    collapse.setAccessible(true);
                    collapse.invoke(service);
                }
                // option 2
                Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                mcontext.sendBroadcast(it);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private final static int REQUEST_CODE = 10101;

    private void checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {

                // If not, form up an Intent to launch the permission request
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                // Launch Intent, with the supplied request code
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if a request code is received that matches that which we provided for the overlay draw request
        if (requestCode == REQUEST_CODE) {

            // Double-check that the user granted it, and didn't just dismiss the request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {

                    // Launch the service
                    launchMainService();
                }
                else {

                    Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}