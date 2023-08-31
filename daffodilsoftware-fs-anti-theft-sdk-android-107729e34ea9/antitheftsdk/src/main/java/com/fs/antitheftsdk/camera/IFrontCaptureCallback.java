package com.fs.antitheftsdk.camera;

public interface IFrontCaptureCallback {

    void onPhotoCaptured(String filePath);

    void onCaptureError(int errorCode);

    enum ErrorCode {}
}