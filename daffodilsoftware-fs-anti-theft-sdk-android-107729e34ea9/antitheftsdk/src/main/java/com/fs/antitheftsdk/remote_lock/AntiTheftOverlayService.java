package com.fs.antitheftsdk.remote_lock;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class AntiTheftOverlayService extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Context ctx=this;

        new Thread() {
            public void run() {
                int i=0;
                boolean run=true;
            while(run){
                    try{Thread.sleep(1000);
                        if(canDrawOverlays()){
                            run=false;
                            NotificationManager nManager = ((NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE));
                            nManager.cancelAll();

                            stopSelf();
                            break;
                        }
                        if (i>120){
                            run=false;
                            stopSelf();
                            break;
                        }
                        i++;
                    }catch (Exception e){

                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);

    }

    private boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return Settings.canDrawOverlays(getApplicationContext());
    }

}