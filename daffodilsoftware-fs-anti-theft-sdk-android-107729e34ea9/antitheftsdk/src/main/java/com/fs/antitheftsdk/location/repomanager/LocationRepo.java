package com.fs.antitheftsdk.location.repomanager;

import com.fs.antitheftsdk.location.model.LocationRequest;
import com.fs.antitheftsdk.login.model.BaseResponse;

import io.reactivex.Observable;

public interface LocationRepo {
    Observable<BaseResponse> sendLocation(LocationRequest requestObj);
}
