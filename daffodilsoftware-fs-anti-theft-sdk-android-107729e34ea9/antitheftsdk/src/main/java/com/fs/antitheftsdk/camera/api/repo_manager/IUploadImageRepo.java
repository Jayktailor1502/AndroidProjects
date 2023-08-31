package com.fs.antitheftsdk.camera.api.repo_manager;

import com.fs.antitheftsdk.camera.api.model.GetApiUrlRequest;
import com.fs.antitheftsdk.camera.api.model.GetApiUrlResponse;

import io.reactivex.Observable;

public interface IUploadImageRepo {

    Observable<GetApiUrlResponse> getImageUrl(GetApiUrlRequest loginReq);
}
