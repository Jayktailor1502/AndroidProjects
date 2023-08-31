package com.fs.antitheftsdk.login.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.apimanager.FsAntiTheftApiErrorHandler;
import com.fs.antitheftsdk.apimanager.FsAntiTheftException;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.base.ErrorCodes;
import com.fs.antitheftsdk.camera.api.callbacks.IImageUploadCallback;
import com.fs.antitheftsdk.camera.api.model.GetApiUrlRequest;
import com.fs.antitheftsdk.camera.api.repo_manager.IUploadImageRepo;
import com.fs.antitheftsdk.camera.api.repo_manager.UploadImageRepoImp;
import com.fs.antitheftsdk.location.callback.ILocationCallback;
import com.fs.antitheftsdk.location.model.LocationRequest;
import com.fs.antitheftsdk.location.repomanager.LocationRepo;
import com.fs.antitheftsdk.location.repomanager.LocationRepoImpl;
import com.fs.antitheftsdk.login.callbacks.IAuthenticationCallback;
import com.fs.antitheftsdk.login.callbacks.IDeviceRegisteredCallback;
import com.fs.antitheftsdk.login.model.AuthenticationRequest;
import com.fs.antitheftsdk.login.model.DeviceDetails;
import com.fs.antitheftsdk.login.model.TokenRequest;
import com.fs.antitheftsdk.login.repomanager.AuthenticationRepoImpl;
import com.fs.antitheftsdk.login.repomanager.IAuthenticationRepo;
import com.fs.antitheftsdk.login.repomanager.IDeviceRegisteredRepo;
import com.fs.antitheftsdk.login.repomanager.IdeviceRegisteredRepoImpl;
import com.fs.antitheftsdk.network.exception.ApiResponse;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;
import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient;
import com.jaredrummler.android.device.DeviceName;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fs.antitheftsdk.base.Constants.TAG;

public class AuthenticationManager {

    private DeviceDetails mDeviceDetails;
    private static AuthenticationManager mRepoManager;
    IAuthenticationRepo tokengen=new AuthenticationRepoImpl();


    public static AuthenticationManager getInstance() {
        if (mRepoManager == null) {

            mRepoManager = new AuthenticationManager();
        }
        return mRepoManager;
    }

    public FsAntiTheftException onHandleServiceError(Throwable throwable) {
        return new FsAntiTheftApiErrorHandler(throwable,
                FsAntiTheftClient.Companion.getContext()).processError();
    }

    private void initDeviceInfoModel() {
        mDeviceDetails = new DeviceDetails();
        mDeviceDetails.setDeviceId(Settings.System.getString(FsAntiTheftClient.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        mDeviceDetails.setDeviceMarketName(Build.MANUFACTURER + " " + Build.DEVICE);
        mDeviceDetails.setDeviceModel(Build.MODEL);
        mDeviceDetails.setDeviceOS(DeviceManagerConstants.DEVICE_OS);
        mDeviceDetails.setDeviceOSVersion(Build.VERSION.RELEASE);
        mDeviceDetails.setDeviceManufacturer(Build.MANUFACTURER);
        mDeviceDetails.setDeviceTotalStorage(getTotalStorage());
        mDeviceDetails.setDeviceTotalRAM(getRAM());
        mDeviceDetails.setDeviceImei(getImeiNumber());
    }


    /**
     * This is the implementation of Authentication process
     */
    @SuppressLint("CheckResult")
    public void callAuthenticateSdk(@Nullable AuthenticationRequest request, IAuthenticationCallback callback, Context context) throws ApiResponse {
        AuthenticationRequest authenticationRequestData = new AuthenticationRequest();
        initDeviceInfoModel();
        String deviceName = DeviceName.getDeviceName();
        IAuthenticationRepo repo = new AuthenticationRepoImpl();
        if (request != null) {
            authenticationRequestData.setEmail(request.getEmail());
            authenticationRequestData.setMobile(request.getMobile());
            authenticationRequestData.setApplicationId(request.getApplicationId());
        }

        authenticationRequestData.setmDeviceId(Settings.Secure.getString(FsAntiTheftClient.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        authenticationRequestData.setmDeviceMarketName(deviceName);
        authenticationRequestData.setmDeviceModel(Build.MODEL);
        authenticationRequestData.setmDeviceOS(DeviceManagerConstants.DEVICE_OS);
        authenticationRequestData.setmDeviceOSVersion(Build.VERSION.RELEASE);
        authenticationRequestData.setmDeviceManufacturer(Build.MANUFACTURER);
        authenticationRequestData.setmDeviceTotalStorage(getTotalStorage());
        authenticationRequestData.setmDeviceTotalRAM(getRAM());
        authenticationRequestData.setmDeviceImei(AppPreferences.getInstance().getString(AppPreferenceKey.EMEI));

        repo.login(authenticationRequestData).subscribe(response -> {

            try {
                callback.onAuthenticationSuccess(response);
            } catch (ApiResponse e) {
                Log.e(TAG,e.toString());
            }
        }, throwable -> {

            ApiResponse apiResponse = throwable instanceof ApiResponse ? (ApiResponse) throwable : null;
            if (callback != null) {
                FsAntiTheftException errorResponse = new FsAntiTheftException(apiResponse != null ? apiResponse.getErrorCode() : context.getString(R.string.technical_error_msg));
                callback.onAuthenticationFail(errorResponse);
            }
        });
    }


    public void saveFirebaseToken(TokenRequest sendRequest, IAuthenticationCallback iAuthenticationCallback) throws ApiResponse {
        IAuthenticationRepo repo = new AuthenticationRepoImpl();
        repo.saveToken(sendRequest).subscribe(sendOtpResponse -> {
            try {
                iAuthenticationCallback.onTokenSuccess(sendOtpResponse);
            } catch (ApiResponse e) {
                Log.e(TAG,e.toString());
            }
        }, throwable ->
            iAuthenticationCallback.onAuthenticationFail(onHandleServiceError(throwable))
        );

    }



    /**
     * method to get Total external storage of Device.
     *
     * @return return string formatted external storage
     */
    private String getTotalStorage() {
        long bytesAvailable;
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        bytesAvailable = stat.getBlockSizeLong() * stat.getBlockCountLong();
        return convertBytesToString(bytesAvailable);
    }

    /**
     * get RAM info using MemoryInfo.
     *
     * @return return string formatted memory of RAM
     */
    private String getRAM() {
        ActivityManager activityManager = (ActivityManager) FsAntiTheftClient.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem;
        return convertBytesToString(totalMemory);
    }

    /**
     * method to convert Bytes into readable format.
     *
     * @param memoryInBytes memory bytes in long
     * @return string formatted memory
     */
    private String convertBytesToString(long memoryInBytes) {
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        String finalValue;
        double kb = memoryInBytes / 1024.0;
        double mb = memoryInBytes / 1048576.0;
        double gb = memoryInBytes / 1073741824.0;
        double tb = memoryInBytes / 1099511627776.0;
        if (tb > 1) {
            finalValue = twoDecimalForm.format(tb).concat(DeviceManagerConstants.MEMORY_IN_TB);
        } else if (gb > 1) {
            finalValue = twoDecimalForm.format(gb).concat(DeviceManagerConstants.MEMORY_IN_GB);
        } else if (mb > 1) {
            finalValue = twoDecimalForm.format(mb).concat(DeviceManagerConstants.MEMORY_IN_MB);
        } else if (kb > 1) {
            finalValue = twoDecimalForm.format(mb).concat(DeviceManagerConstants.MEMORY_IN_KB);
        } else {
            finalValue = twoDecimalForm.format(memoryInBytes).concat(DeviceManagerConstants.MEMORY_IN_BYTES);
        }
        return finalValue;
    }

    /**
     * method to fetch IMEI number if Android Api version is below Q(29).
     *
     * @return string value. Imei number or empty value.
     */
    private String getImeiNumber() {
        String imei = mDeviceDetails.getDeviceImei();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (Boolean.TRUE.equals(checkPermission())) {
                TelephonyManager telephonyManager = (TelephonyManager) FsAntiTheftClient.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    imei = telephonyManager.getDeviceId();
                }
            } else {
                imei = Settings.Secure.getString(FsAntiTheftClient.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        if (imei == null) {
            imei = Settings.Secure.getString(FsAntiTheftClient.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return imei;
    }

    @SuppressLint("CheckResult")
    public void sendLocationRequest(LocationRequest locationRequest, ILocationCallback iLocationCallback, Context context) throws ApiResponse {
        LocationRepo repo = new LocationRepoImpl();
        repo.sendLocation(locationRequest).subscribe(response -> {
            try {
                iLocationCallback.onLocationSuccess(response);
            } catch (ApiResponse e) {
                Log.d(Constants.ERROR, e.toString());
            }
        }, throwable -> {

            ApiResponse apiResponse = throwable instanceof ApiResponse ? (ApiResponse) throwable : null;
            if(apiResponse!=null) {
                if (apiResponse.getErrorCode().contains(ErrorCodes.errorUNAUTHORIZED)) {

                    Log.v(TAG, "Token expired hitting api");

                    tokengen.generateToken(AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID) + "").enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            try {
                                sendLocationRequest(locationRequest, iLocationCallback, context);


                            } catch (ApiResponse e) {
                                Log.e(TAG, e.toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });


                }

            else {
                if (iLocationCallback != null) {
                    FsAntiTheftException errorResponse = new FsAntiTheftException(apiResponse != null ? apiResponse.getErrorCode() : context.getString(R.string.technical_error_msg));
                    iLocationCallback.onLocationFail(errorResponse);
                }
            }
            }
        });
    }




    @SuppressLint("CheckResult")
    public void getUploadImageUrl(@Nullable GetApiUrlRequest request, IImageUploadCallback callback,Context context) throws ApiResponse {
        GetApiUrlRequest getImageRequestData = new GetApiUrlRequest();
        IUploadImageRepo repo = new UploadImageRepoImp();
        if (request != null) {
            getImageRequestData.setImageType(request.getImageType());
            getImageRequestData.setUserId(request.getUserId());
        }

        repo.getImageUrl(getImageRequestData).subscribe(response -> {

            try {
                callback.onGetUrlSuccess(response);
            } catch (ApiResponse e) {
                throw new Exception();
            }
        }, throwable -> {

            ApiResponse apiResponse = throwable instanceof ApiResponse ? (ApiResponse) throwable : null;

            if(apiResponse!=null) {
                if (apiResponse.getErrorCode().contains(ErrorCodes.errorUNAUTHORIZED)) {
                    Log.v(TAG, "Token expired hitting api");

                    tokengen.generateToken(AppPreferences.getInstance().getInt(AppPreferenceKey.USER_ID) + "").enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            try {
                                getUploadImageUrl(request, callback, context);


                            } catch (ApiResponse e) {
                                Log.e(TAG, e.toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });

                }

            else {
                if (callback != null) {
                    FsAntiTheftException errorResponse = new FsAntiTheftException(apiResponse != null ? apiResponse.getErrorCode() : context.getString(R.string.technical_error_msg));
                    callback.onGetUrlFail(errorResponse);
                }
            }
            }
        });

    }

    public void checkDeviceRegister(Context ctx, IDeviceRegisteredCallback iDeviceRegisteredCallback) throws ApiResponse {
        IDeviceRegisteredRepo repo = new IdeviceRegisteredRepoImpl();
        String deviceId = Settings.System.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        AppPreferences.getInstance().putString(AppPreferenceKey.DEVICE_ID, deviceId);
        repo.checkDeviceRegister(deviceId).subscribe(response -> {
            try {
                iDeviceRegisteredCallback.onRegisterSuccess(response);
            } catch (ApiResponse e) {
                Log.e(TAG,e.toString());
            }
        }, throwable ->
            iDeviceRegisteredCallback.onRegisterFail(onHandleServiceError(throwable))
        );
    }

    public static Boolean checkPermission(){
        return ContextCompat.checkSelfPermission(FsAntiTheftClient.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }
}

