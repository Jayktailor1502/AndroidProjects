package com.fs.antitheftsdk.login.workmanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.apimanager.FsAntiTheftException;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.battery.BatteryWorker;
import com.fs.antitheftsdk.login.callbacks.IAuthenticationCallback;
import com.fs.antitheftsdk.login.model.AuthenticationRequest;
import com.fs.antitheftsdk.login.model.AuthenticationResponse;
import com.fs.antitheftsdk.login.model.BaseResponse;
import com.fs.antitheftsdk.network.exception.ApiResponse;
import com.fs.antitheftsdk.prefs.AppPreferenceKey;
import com.fs.antitheftsdk.prefs.AppPreferences;
import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient;

import static com.fs.antitheftsdk.base.Constants.TAG;

/*This class is login worker to hit the Registeration api from outside the sdk*/
public class LoginWorker extends Worker {

    Context mContext;
    String email;
    String mobile ;
    String applicationId;
    private static final String PROGRESS = "PROGRESS";

    public LoginWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        AppPreferences.init(mContext);

    }


    @NonNull
    @Override
    public Result doWork() {
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
        email = getInputData().getString(Constants.EMAIL);
        mobile = getInputData().getString(Constants.MOBILE);
        applicationId = getInputData().getString(Constants.APPLICATION_ID);
        try {
            authentication(email, mobile, applicationId);
        } catch (ApiResponse e) {
            Log.e(TAG,e.toString());
        }
        return Result.success();
    }


    public void authentication(String email, String mobile, String applicationId) throws ApiResponse {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setMobile(mobile);
        authenticationRequest.setApplicationId(applicationId);
        FsAntiTheftClient.getInstance(getApplicationContext()).callAuthenticateSdk(authenticationRequest, new IAuthenticationCallback() {
            @Override
            public void onAuthenticationFail(FsAntiTheftException response) {
            }

            @Override
            public void onAuthenticationSuccess(AuthenticationResponse response) throws ApiResponse {
                AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOGGED_IN, true);
                AppPreferences.getInstance().putString(AppPreferenceKey.EMAIL, response.getEmail());
                AppPreferences.getInstance().putString(AppPreferenceKey.MOBILE, response.getMobile());
                if (response.getUserId() != null) {
                    AppPreferences.getInstance().putInt(AppPreferenceKey.USER_ID, response.getUserId());
                }
                setProgressAsync(new Data.Builder().putInt(PROGRESS, 100).build());
                AppPreferences.getInstance().putBoolean(AppPreferenceKey.IS_LOGGED_IN, true);
                WorkManager mWorkManager = WorkManager.getInstance(mContext);
                mWorkManager.enqueue(OneTimeWorkRequest.from(BatteryWorker.class));
                Intent broadcastIntent = new Intent(Constants.BROADCAST_LOGIN);
                broadcastIntent.putExtra(Constants.REQUEST_EXTRA_LOGIN, true);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcastIntent);
            }

            @Override
            public void onTokenSuccess(BaseResponse response) throws ApiResponse {
                Log.d(TAG, mContext.getString(R.string.token_Success));
            }
        },mContext);

    }

    private void showToast(String string) {
        if (string != null)
            Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }


}
