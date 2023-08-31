package com.fs.antitheftsdk.network.Rx;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.network.exception.ApiResponse;
import com.fs.antitheftsdk.network.exception.NoConnectivityException;
import com.fs.antitheftsdk.network.network.NetworkConstants;
import com.google.gson.JsonSyntaxException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

public class ApiErrorHelper<T> {
    public Function<Throwable, Observable<? extends T>> getErrorParser() {
        return new Function<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> apply(Throwable throwable) throws Exception {
                if (throwable instanceof NoConnectivityException) {
                    return Observable.error(throwable);
                }

                ApiResponse exception = null;

                if (throwable instanceof HttpException) {
                    String errorBody = ((HttpException) throwable).response().errorBody().string();

                    try {
                        exception = new ApiResponse(errorBody, R.string.network_error_msg);

                    } catch (JsonSyntaxException e) {
                        exception = new ApiResponse(NetworkConstants.ErrorCode.PARSING_ERROR, R.string.technical_error_msg);
                    }

                }
                if (exception == null) {
                    exception = new ApiResponse(NetworkConstants.ErrorCode.SOME_ERROR_OCCURED, R.string.technical_error_msg);
                }

                return Observable.error(exception);
            }
        };
    }

}
