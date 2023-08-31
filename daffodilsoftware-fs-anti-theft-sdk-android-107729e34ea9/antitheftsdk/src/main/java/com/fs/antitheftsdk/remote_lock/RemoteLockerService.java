package com.fs.antitheftsdk.remote_lock;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.adapters.TextViewBindingAdapter;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.activity.BlankActivity;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.camera.GetBackCoreService;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;
import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient;

import java.util.Objects;


public class RemoteLockerService extends Service implements View.OnTouchListener, TextViewBindingAdapter.OnTextChanged {

  private static final String TAG = RemoteLockerService.class.getSimpleName();

  private WindowManager windowManager;

  private View floatyView;
  EditText pass;
  TextView txt_error;
  Button enter;
  static String enc_key;
  static int wrongpasscount=0;
  LinearLayoutCompat background;
  @Override
  public IBinder onBind(Intent intent) {

    return null;
  }

  @Override
  public void onCreate() {

    super.onCreate();

    windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    addOverlayView();

    new BlockStatusBar(FsAntiTheftClient.getContext(),true);
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    super.onTaskRemoved(rootIntent);
  }

  @Override
  public boolean stopService(Intent name) {
    return super.stopService(name);
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if(intent!=null) {
      enc_key = intent.getStringExtra("key");
    }

    return START_STICKY;
  }

  private void addOverlayView() {

    final LayoutParams params;


    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
    layoutParams.format = PixelFormat.TRANSLUCENT;
    layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    } else {
      layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
    }



    FrameLayout interceptorLayout = new FrameLayout(this) {

      @Override
      public boolean dispatchKeyEvent(KeyEvent event) {

        // Only fire on the ACTION_DOWN event, or you'll get two events (one for _DOWN, one for _UP)
        if (event.getAction() == KeyEvent.ACTION_DOWN) {

          // Check if the HOME button is pressed
          if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            Log.v(TAG, "BACK Button Pressed");

            // As we've taken action, we'll return true to prevent other apps from consuming the event as well
            return true;
          }

        }

        // Otherwise don't intercept the event
        return super.dispatchKeyEvent(event);
      }
    };

    AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOCKED_STATUS,true);
    LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

    if (inflater != null) {
      floatyView = inflater.inflate(R.layout.lock_screen, interceptorLayout);
      /*floatyView.setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                      | View.SYSTEM_UI_FLAG_FULLSCREEN
                      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
*/
     floatyView.setOnTouchListener(this);


      pass=floatyView.findViewById(R.id.pin_editext);
      txt_error=floatyView.findViewById(R.id.txt_error);
      background=floatyView.findViewById(R.id.background);

      enter=floatyView.findViewById(R.id.enter);

      pass.addTextChangedListener(new TextWatcher() {
        @SuppressLint("SetTextI18n")
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          if (charSequence.length()<7)
          {
            enter.setEnabled(false);
            enter.setBackground(getDrawable(R.drawable.ic_rectangle_grey));
            txt_error.setVisibility(View.INVISIBLE);
            background.setBackground(getResources().getDrawable(R.drawable.rectangle,null));


          }
          else
          {
            enter.setEnabled(true);
            enter.setBackground(getDrawable(R.drawable.rectangle_blue));
          }
          if(charSequence.length()==0)
          {
            txt_error.setVisibility(View.INVISIBLE);
            enter.setEnabled(false);
            enter.setBackground(getDrawable(R.drawable.ic_rectangle_grey));
            background.setBackground(getResources().getDrawable(R.drawable.rectangle,null));

          }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
      });
      if(enter!=null){
      enter.setOnClickListener(new View.OnClickListener() {
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
          if(!pass.getText().toString().isEmpty()) {
            String passcode = pass.getText().toString();
            if (enc_key != null) {
              if (!enc_key.isEmpty() || !enc_key.equals("")) {
                passcode=removeChars(passcode);
                if (passcode.equals(enc_key)) {
                  txt_error.setVisibility(View.INVISIBLE);
                  // Kill service
                  onDestroy();
                  AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOCKED_STATUS, false);
                } else {
                  txt_error.setVisibility(View.VISIBLE);

                  background.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_red,null));
                  wrongpasscount++;
                  txt_error.setText(getResources().getString(R.string.wrong_pass));

                  if(wrongpasscount==3)
                  {
                    pass.setEnabled(false);
                    pass.setText("");
                    enter.setEnabled(false);
                    enter.setBackground(getDrawable(R.drawable.ic_rectangle_grey));
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(getResources().getString(R.string.wrong_pass_3x));

                    Handler handler=new Handler();
                    Runnable r=new Runnable() {
                      public void run() {
                        //what ever you do here will be done after 3 seconds delay.
                        pass.setEnabled(true);

                        txt_error.setVisibility(View.INVISIBLE);
                      }
                    };
                    handler.postDelayed(r, 600000);
                  }
                  if(wrongpasscount==4)
                  {
                    pass.setEnabled(false);
                    pass.setText("");
                    enter.setEnabled(false);
                    enter.setBackground(getDrawable(R.drawable.ic_rectangle_grey));
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(getResources().getString(R.string.wrong_pass_4x));

                    if (Build.VERSION.SDK_INT > 29) {

                      Constants.TriggerStatus=true;
                      Intent i = new Intent(FsAntiTheftClient.getContext(), BlankActivity.class);
                      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                      FsAntiTheftClient.getContext().startActivity(i);
                    } else {
                      Objects.requireNonNull(FsAntiTheftClient.getContext()).startForegroundService(new Intent(FsAntiTheftClient.getContext(), GetBackCoreService.class));

                    }

                    Handler handler=new Handler();
                    Runnable r=new Runnable() {
                      public void run() {
                        //what ever you do here will be done after 3 seconds delay.
                        pass.setEnabled(true);

                        txt_error.setVisibility(View.INVISIBLE);
                      }
                    };
                    handler.postDelayed(r, (long) 1.8e+6);
                  }
                  else if(wrongpasscount==5)
                  {

                      pass.setEnabled(false);
                      pass.setText("");
                      enter.setEnabled(false);
                      enter.setBackground(getDrawable(R.drawable.ic_rectangle_grey));
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText(getResources().getString(R.string.wrong_pass_5x));

                      Handler handler=new Handler();
                      Runnable r=new Runnable() {
                        public void run() {
                          //what ever you do here will be done after 3 seconds delay.
                          pass.setEnabled(true);

                          txt_error.setVisibility(View.INVISIBLE);
                        }
                      };
                      handler.postDelayed(r, (long) 3.6e+6);

                  }
                  }
                }
              }
            }
            else {

              txt_error.setVisibility(View.VISIBLE);
            background.setBackground(getResources().getDrawable(R.drawable.ic_rectangle_red,null));

            wrongpasscount++;
            }


        }
      });
      }


      windowManager.addView(floatyView, layoutParams);
    }
    else {
      Log.e("SAW-example", "Layout Inflater Service is null; can't inflate and display R.layout.floating_view");
    }
  }


  @Override
  public void onDestroy() {

    super.onDestroy();

    if (floatyView != null) {

      windowManager.removeView(floatyView);

      floatyView = null;
    }
  }


  @Override
  public String getOpPackageName() {
    return super.getOpPackageName();
  }


  private String removeChars(String s)
  {

    return s.replaceAll("[^a-zA-Z0-9]", "");
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    view.performClick();

    Log.v(TAG, "onTouch...");

    // Kill service
    //onDestroy();

    return true;
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {


  }
}
