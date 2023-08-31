package com.fs.antitheftsdk.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.camera.work_manager.CameraWorker;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.content.Context.WINDOW_SERVICE;

public class CameraView implements SurfaceHolder.Callback, PictureCallback, ErrorCallback {

    private Context context = null;
    private SurfaceHolder sHolder;
    private static Camera camera = null;
    private AudioManager audioMgr = null;
    private IFrontCaptureCallback callback;
    private SurfaceView surfaceView = null;
    static ArrayList<String> imagesList = null;
    String path  = "";

    public CameraView(Context ctx) {
        context = ctx;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onError(int error, Camera camera) {
        Log.d(TAG, context.getString(R.string.camera_error) + error, null);

        WindowManager window = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        window.removeView(surfaceView);
        callback.onCaptureError(-1);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        if (data != null) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
                audioMgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);

            WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            manager.removeView(surfaceView);

            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length, opts);
                bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int newWidth = 300;
                int newHeight = 300;

                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                if (Build.VERSION.SDK_INT > 29) {
                    matrix.postRotate(-90);
                }
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

                Uri resizedUri = getImageUri(resizedBitmap);


                if (imagesList == null) {
                    imagesList = new ArrayList<>();
                    imagesList.add(resizedUri.toString());
                } else {
                    imagesList.add(resizedUri.toString());
                }

                callCameraWorker();

            } catch (Exception e) {
                Log.e(TAG,e.toString());
            }
        }
    }

    private void startCamera(){
        surfaceView = new SurfaceView(context);
        WindowManager winMan = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        try {

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1, layoutFlag, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            winMan.addView(surfaceView, params);

            surfaceView.setZOrderOnTop(true);

            SurfaceHolder holder = surfaceView.getHolder();

            holder.setFormat(PixelFormat.TRANSPARENT);

            sHolder = holder;
            sHolder.addCallback(this);
            sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            com.fs.antitheftsdk.camera.Utils.LogUtil.logD(Constants.LOG_TAG, context.getString(R.string.opening_camera));

          try{
                camera = Camera.open(1);
            }
          catch (Exception e)
          {
              e.printStackTrace();

          }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 40, stream);
        byte[] byteArray = stream.toByteArray();

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File file = null;
        try {
            file = File.createTempFile(timeStamp, ".png", storageDir);
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }

        try(FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(byteArray);
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }
        if (file != null){
            path = file.getPath();
        }
        return Uri.parse(path);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
            camera.startPreview();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
                audioMgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            Log.d(TAG, context.getString(R.string.taking_picture));

            camera.takePicture(null, null, this);

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
            if (info.canDisableShutterSound) {
                camera.enableShutterSound(false);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera != null) {
            try {
                Log.d(TAG, context.getString(R.string.camera_opened));
                camera.setPreviewDisplay(sHolder);
            } catch (IOException exception) {
                camera.release();
                camera = null;
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        Log.d(TAG, context.getString(R.string.camera_released));
        camera = null;
        if (imagesList.size() < 5){
            startCamera();
        }
        if (imagesList.size() == 5){
            imagesList = null;
        }
    }

    public void capturePhoto(IFrontCaptureCallback frontCaptureCb) {

        callback = frontCaptureCb;

        if (!com.fs.antitheftsdk.camera.Utils.isFrontCameraPresent(context,camera))
            callback.onCaptureError(-1);

        startCamera();
    }

    public void callCameraWorker() throws IOException {
        if (imagesList.size() == 5) {
            AppPreferences.getInstance().putString(AppPreferenceKey.URI, ObjectSerializer.serialize(imagesList));

            Data input = new Data.Builder()
                    .putInt(com.fs.antitheftsdk.base.Constants.USER_ID, AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID))
                    .putString(com.fs.antitheftsdk.base.Constants.IMAGE_TYPE, com.fs.antitheftsdk.base.Constants.IMAGE_TYPE_PNG).build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CameraWorker.class)
                    .setInputData(input)
                    .build();
            WorkManager.getInstance(context).enqueueUniqueWork(com.fs.antitheftsdk.base.Constants.CAMERA_MANAGER, ExistingWorkPolicy.REPLACE, workRequest);
        }
    }
}