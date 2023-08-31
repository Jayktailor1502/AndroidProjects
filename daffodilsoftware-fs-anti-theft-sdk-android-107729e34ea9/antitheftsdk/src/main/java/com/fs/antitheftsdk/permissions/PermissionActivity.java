package com.fs.antitheftsdk.permissions;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fs.antitheftsdk.adapter.RecyclerViewAdapter;
import com.fs.antitheftsdk.R;

public class PermissionActivity extends AppCompatActivity implements IRequestPermissionCallback {

    Button button;
    Context context;
    String[] permissions;
    RecyclerView permissionList;
    RecyclerViewAdapter recyclerViewAdapter;
    IRequestPermissionCallback iRequestPermissionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        context = this;
        iRequestPermissionCallback = this;

        permissions = new String[]{"Admin", "DisplayOverlay", "Location", "Camera"};
        permissionList = (RecyclerView) findViewById(R.id.recyclerList_1);
        recyclerViewAdapter = new RecyclerViewAdapter(permissions, context);
        permissionList.setLayoutManager(new LinearLayoutManager(context));
        permissionList.setAdapter(recyclerViewAdapter);

        button = findViewById(R.id.permission_btn);
        button.setOnClickListener((view ->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Boolean.FALSE.equals(PermissionsUtil.checkCameraPermission(context))) {
                    PermissionsUtil.requestCameraPermission(context, iRequestPermissionCallback);
                }
                if (Boolean.FALSE.equals(PermissionsUtil.checkLocationPermission(context))) {
                    PermissionsUtil.requestLocationPermission(context, iRequestPermissionCallback);
                }
                requestPermissionAdmin();
                requestPermissionOverlay();
            }
        } ));
    }


    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewAdapter.notifyDataSetChanged();

    }

    public void requestPermissionAdmin() {
        if (Boolean.FALSE.equals(PermissionsUtil.checkadminPermission(context))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getString(R.string.admin_rational))
                    .setTitle("")
                    .setPositiveButton(getString(R.string.yes),(di,i) ->
                        PermissionsUtil.requestAdminPermission(context, iRequestPermissionCallback))
                    .setNegativeButton(getString(R.string.no),(di,i) ->
                        Toast.makeText(context, getString(R.string.admin_not_granted), Toast.LENGTH_SHORT).show()
                    ).create().show();
        }
    }

    public void requestPermissionOverlay() {
        if (Boolean.FALSE.equals(PermissionsUtil.checkDisplayOverlayPermission(context))) {
            PermissionsUtil.requestOverlayPermission(context, iRequestPermissionCallback);
        }
    }

    //This method is the callback method for permission request of location
    @Override
    public void callbackLocation(Boolean locationPermissionstatus) {
    }

    //This method is the callback method for permission request of Camera
    @Override
    public void callbackCamera(Boolean cameraPermissionStatus) {
    }


}