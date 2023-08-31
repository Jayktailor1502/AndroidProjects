package com.fs.antitheftsdk.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.camera.GetBackCoreService;

//This activity is used during locked state to get click pictures
public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.TriggerStatus) {
            showWhenLockedAndTurnScreenOn();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                final Window win = getWindow();
                win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            if (Build.VERSION.SDK_INT > 25) {
                startForegroundService(new Intent(this, GetBackCoreService.class));

            } else {
                startService(new Intent(this, GetBackCoreService.class));
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Constants.TriggerStatus=false;
                    BlankActivity.this.finish();
                }
            }, 10000);
        }
        else
        {
            Constants.TriggerStatus=false;
            finish();

        }


    }

    @Override
    protected void onDestroy() {
       super.onDestroy();

    }

    private void showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            this.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            );
        }
    }

}
