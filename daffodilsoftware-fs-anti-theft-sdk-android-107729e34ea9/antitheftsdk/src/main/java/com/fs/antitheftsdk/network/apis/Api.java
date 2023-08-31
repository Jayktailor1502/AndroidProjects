package com.fs.antitheftsdk.network.apis;

import com.fs.antitheftsdk.camera.api.model.GetApiUrlResponse;
import com.fs.antitheftsdk.location.model.LocationRequest;
import com.fs.antitheftsdk.login.model.AuthenticationRequest;
import com.fs.antitheftsdk.login.model.AuthenticationResponse;
import com.fs.antitheftsdk.login.model.BaseResponse;
import com.fs.antitheftsdk.login.model.DeviceRegisteredResponse;
import com.fs.antitheftsdk.login.model.TokenRequest;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * All API's should go here
 */

public interface Api {
    @POST("public/v1/user/signup")
    Observable<AuthenticationResponse> authenticate(@Body AuthenticationRequest request, @Header("apiKey") String apiKey);

    @POST("v1/location")
    Observable<BaseResponse> sendLocation(@Body LocationRequest request, @Header("apiKey") String apiKey, @Header("Authorization") String Authorization);

    @GET("v1/upload")
    Observable<GetApiUrlResponse> getImageUrl(@Query("userId") Integer userId, @Query("imageType") String imageType, @Header("apiKey") String apiKey, @Header("Authorization") String Authorization);

    @POST("public/v1/deviceToken")
    Observable<BaseResponse> saveToken(@Body TokenRequest request, @Header("apiKey") String apiKey);

    @GET("public/v1/register/{deviceId}")
    Observable<DeviceRegisteredResponse> checkDeviceRegistered(@Path("deviceId") String deviceId, @Header("apiKey") String apiKey);


    @GET("public/v1/generateToken/{userId}")
    Call<Void> generateToken(@Path("userId") String userId, @Header("apiKey") String apiKey, @Header("Authorization") String Authorization);
}


