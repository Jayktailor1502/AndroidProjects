package com.fs.antitheftsdk.camera.api.callbacks;

import com.fs.antitheftsdk.apimanager.FsAntiTheftException;
import com.fs.antitheftsdk.camera.api.model.GetApiUrlResponse;
import com.fs.antitheftsdk.network.exception.ApiResponse;

public interface IImageUploadCallback {

    void onGetUrlSuccess(GetApiUrlResponse response) throws ApiResponse;
    void onGetUrlFail(FsAntiTheftException response);
}
