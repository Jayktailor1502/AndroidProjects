package com.fs.antitheftsdk.permissions;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.core.content.ContextCompat;

import com.fs.antitheftsdk.base.Constants;

import java.util.List;

import static android.content.Context.DEVICE_POLICY_SERVICE;

public class PermissionsUtil {
    private static ComponentName componentName;
    private static DevicePolicyManager devicePolicyManager;

    private PermissionsUtil() {
    }

    public static Boolean checkLocationPermission(Context context) {
        int resultFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int resultCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            return resultFineLocation == PackageManager.PERMISSION_GRANTED && resultCoarseLocation == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
        } else {
            return resultFineLocation == PackageManager.PERMISSION_GRANTED && resultCoarseLocation == PackageManager.PERMISSION_GRANTED;
        }

    }

    public static Boolean checkDisplayOverlayPermission(Context context) {
        boolean isTrue = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isTrue= Settings.canDrawOverlays(context);
        }

        if(isTrue&&isAccessibilityServiceEnabled(context))
        {

        }

        return isTrue;
    }

    public static Boolean checkCameraPermission(Context context) {

        int resultCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

        return resultCamera == PackageManager.PERMISSION_GRANTED;
    }

    public static Boolean checkPhoneStatePermission(Context context) {

        int resultCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

        return resultCamera == PackageManager.PERMISSION_GRANTED;
    }

    public static Boolean checkadminPermission(Context context) {

        componentName = new ComponentName(context, Admin.class);
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);

        return devicePolicyManager.isAdminActive(componentName);
    }

    public static Boolean checkSinglePermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestAdminPermission(Context context, IRequestPermissionCallback iRequestPermissionCallback) {

        PermissionPopup.initCallback(iRequestPermissionCallback);
        Intent i = new Intent(context, PermissionPopup.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.PERMISSION_TYPE, Constants.ADMIN);
        context.startActivity(i);

    }

    public static void requestLocationPermission(Context context, IRequestPermissionCallback iRequestPermissionCallback) {

        PermissionPopup.initCallback(iRequestPermissionCallback);
        Intent i = new Intent(context, PermissionPopup.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.PERMISSION_TYPE, Constants.LOCATION);
        context.startActivity(i);
    }


    public static void requestCameraPermission(Context context, IRequestPermissionCallback iRequestPermissionCallback) {

        PermissionPopup.initCallback(iRequestPermissionCallback);
        Intent i = new Intent(context, PermissionPopup.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.PERMISSION_TYPE, Constants.CAMERA);
        context.startActivity(i);
    }

    public static void requestOverlayPermission(Context context, IRequestPermissionCallback iRequestPermissionCallback) {

        PermissionPopup.initCallback(iRequestPermissionCallback);
        Intent i = new Intent(context, PermissionPopup.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.PERMISSION_TYPE, Constants.OVERLAY);
        context.startActivity(i);
    }
    public static void requestAccessibility(Context context,IRequestPermissionCallback iRequestPermissionCallback)
    {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isAccessibilityServiceEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()))
                return true;
        }

        return false;
    }

    public static void redirectToSettings(Context context) {

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public static void showGPSDisabledAlertToUser(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                callGPSSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}