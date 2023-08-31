package com.fs.antitheftsdk.permissions;

public interface IRequestPermissionCallback {

    void callbackLocation(Boolean locationPermissionstatus);

    void callbackCamera(Boolean cameraPermissionStatus);

}
