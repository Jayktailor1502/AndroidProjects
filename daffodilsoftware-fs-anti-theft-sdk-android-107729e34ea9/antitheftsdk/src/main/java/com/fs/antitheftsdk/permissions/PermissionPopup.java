package com.fs.antitheftsdk.permissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.base.Constants;

public class PermissionPopup extends AppCompatActivity {

    private static ComponentName componentName;
    private String permissionType;
    private Context context;
    private static IRequestPermissionCallback requestPermissionCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        if (getIntent() != null) {
            permissionType = getIntent().getStringExtra(Constants.PERMISSION_TYPE);
        }
        permissionCheck();


    }

    public void permissionCheck() {
        switch (permissionType) {
            case Constants.LOCATION:
                locationPermission();
                break;

            case Constants.CAMERA:
                cameraPermission();
                break;

            case Constants.ADMIN:
                adminPermission();
                finish();
                break;

            case Constants.OVERLAY:
                permissionOverlay();
                finish();
                break;

        }

    }

    public static void initCallback(IRequestPermissionCallback iRequestPermissionCallback) {
        requestPermissionCallback = iRequestPermissionCallback;
    }

    public void permissionOverlay() {

        if (Boolean.FALSE.equals(PermissionsUtil.checkDisplayOverlayPermission(this))) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
            else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        finish();
    }


    public void cameraPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);

    }

    public void locationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }


    public void courseLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 4);

    }


    public void adminPermission() {
        componentName = new ComponentName(this, Admin.class);


        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.device_admin_explanation)
        );
        startActivity(intent);
        finish();

    }


    @TargetApi(30)
    private void checkBackgroundLocationPermissionAPI30(int backgroundLocationRequestCode) {
        if (Boolean.FALSE.equals(PermissionsUtil.checkSinglePermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION))) {


            String[] permissions = {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(getString(R.string.location_heading))
                    .setMessage(getString(R.string.access_bg_loc_message))
                    .setPositiveButton(this.getString(R.string.yes), (di,i) ->{
                        ActivityCompat.requestPermissions(PermissionPopup.this, permissions, 5);
                        di.dismiss();
                    })
                    .setNegativeButton(getString(R.string.no), (di,i) -> {
                        requestPermissionCallback.callbackLocation(false);
                        di.dismiss();
                        finish();
                    })
                    .create()
                    .show();

        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length <= 0) {


                    finish();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        courseLocationPermission();

                    }
                    else {
                        requestPermissionCallback.callbackLocation(true);
                        finish();
                    }

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.location_rational))
                                    .setCancelable(false)
                                    .setTitle(getString(R.string.location_heading))
                                    .setPositiveButton(getString(R.string.yes),(di,i) ->{
                                        locationPermission();
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                        Toast.makeText(context, getString(R.string.location_rational), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }).create().show();

                        }
                        //This else part is for the scenario when rational is false and permission is never ask
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.location_rational))
                                    .setTitle(getString(R.string.location_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes),(di,i) ->{
                                        PermissionsUtil.redirectToSettings(context);
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                        Toast.makeText(context, getString(R.string.location_rational), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }).create().show();
                        }

                    }
                    else
                    {
                        requestPermissionCallback.callbackLocation(false);
                        finish();
                    }

                }

                break;
            }

            case 2: {
                if (grantResults.length <= 0) {

                    requestPermissionCallback.callbackCamera(false);

                    finish();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestPermissionCallback.callbackCamera(true);
                    finish();
                } else {
                    if (Boolean.FALSE.equals(PermissionsUtil.checkCameraPermission(context)) && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.camera_rational))
                                    .setTitle(getString(R.string.camera_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes), (di,i) ->{
                                        cameraPermission();
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                        Toast.makeText(context, getString(R.string.camera_rational), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }).create().show();
                        }
                        //This else part is for the scenario when rational is false and permission is never ask
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.camera_rational))
                                    .setTitle(getString(R.string.camera_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes), (di,i) ->{
                                        PermissionsUtil.redirectToSettings(context);
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                        Toast.makeText(context, getString(R.string.camera_rational), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }).create().show();
                        }
                    }
                    requestPermissionCallback.callbackCamera(false);
                }

                break;
            }

            case 3: {
                if (grantResults.length <= 0) {

                    requestPermissionCallback.callbackLocation(false);
                    finish();
                } else if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestPermissionCallback.callbackLocation(grantResults[0] == PackageManager.PERMISSION_GRANTED);

                }
                else
                {
                    PermissionsUtil.redirectToSettings(context);
                    requestPermissionCallback.callbackLocation(false);

                }
                    finish();
                break;
            }

            case 4: {
                if (grantResults.length <= 0) {

                    finish();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        checkBackgroundLocationPermissionAPI30(3);
                    }

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.location_rational))
                                    .setTitle(getString(R.string.location_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes),(di,i) ->{
                                        locationPermission();
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                        requestPermissionCallback.callbackLocation(false);
                                        finish();
                                    }).create().show();

                        }
                        //This else part is for the scenario when rational is false and permission is never ask
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.location_rational))
                                    .setTitle(getString(R.string.location_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes), (di,i) ->{
                                        PermissionsUtil.redirectToSettings(context);
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) -> {
                                        requestPermissionCallback.callbackLocation(false);
                                        finish();
                                            }
                                    ).create().show();
                        }

                    }

                }

                break;
            }


            case 5:
            {
                if (grantResults.length <= 0) {

                    finish();
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                        checkBackgroundLocationPermissionAPI30(3);
                    }

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.location_rational))
                                    .setTitle(getString(R.string.location_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes),(di,i) ->{
                                        locationPermission();
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                        requestPermissionCallback.callbackLocation(false);
                                        finish();
                                    }).create().show();

                        }
                        //This else part is for the scenario when rational is false and permission is never ask
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage(getResources().getString(R.string.location_rational))
                                    .setTitle(getString(R.string.location_heading))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.yes), (di,i) ->{
                                        PermissionsUtil.redirectToSettings(context);
                                        finish();
                                    })
                                    .setNegativeButton(getString(R.string.no), (di,i) ->{
                                            requestPermissionCallback.callbackLocation(false);
                                    finish();
                        }
                                    ).create().show();
                        }

                    }

                }

                break;
        }

        }
    }

}
