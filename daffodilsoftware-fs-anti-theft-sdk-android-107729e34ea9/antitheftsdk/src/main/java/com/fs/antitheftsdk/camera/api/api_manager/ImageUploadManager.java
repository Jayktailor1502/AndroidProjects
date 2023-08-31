package com.fs.antitheftsdk.camera.api.api_manager;

import androidx.appcompat.app.AppCompatActivity;

import com.fs.antitheftsdk.apimanager.FsAntiTheftApiErrorHandler;
import com.fs.antitheftsdk.apimanager.FsAntiTheftException;
import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient;

public class ImageUploadManager extends AppCompatActivity {
    private static ImageUploadManager mRepoManager;

    public static ImageUploadManager getInstance() {

        if (mRepoManager == null) {
            mRepoManager = new ImageUploadManager();

        }
        return mRepoManager;
    }

    public FsAntiTheftException onHandleServiceError(Throwable throwable) {
        return new FsAntiTheftApiErrorHandler(throwable,
                FsAntiTheftClient.Companion.getContext()).processError();
    }
}
