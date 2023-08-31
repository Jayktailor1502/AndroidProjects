package com.fs.antitheftsdk.network.Rx;

import android.util.Pair;

import com.fs.antitheftsdk.network.network.NetworkConstants;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Provides exponential back off functionality with Rx Java functions.
 */
public class RetryAPI {
    private RetryAPI() {
    }

    private static final int UNCHECKED_ERROR_TYPE_CODE = -100;

    /**
     * This function applies retry rules to RX Observable with
     * some delay and number of retries configured from firebase
     * <p>
     * Should be used with API's to retry in case of time out error.
     *
     * @return Rx Function with observable
     */
    public static Function<Observable<? extends Throwable>, Observable<?>> retryWithExponentialBackOff() {
        return exponentialBackoffForExceptions(TimeUnit.MICROSECONDS, SocketTimeoutException.class);
    }

    /**
     * Calling this function will re execute the observable or pass on the error if not handled
     *
     * @param unit,      Time unit in milliseconds, seconds etc.
     * @param errorTypes Type of errors callers want to handle and retry for.
     * @return Rx Function for error
     */
    @SafeVarargs
    private static Function<Observable<? extends Throwable>, Observable<?>> exponentialBackoffForExceptions(final TimeUnit unit, final Class<? extends Throwable>... errorTypes) {
        final long initialDelay = NetworkConstants.DEFAULT_INITIAL_DELAY;
        final int numRetries = NetworkConstants.DEFAULT_NUM_RETRIES;
        return errors -> errors
                .zipWith(Observable.range(1, numRetries + 1), (error, integer) -> {
                    if (integer == numRetries + 1) {
                        return new Pair<>(error, UNCHECKED_ERROR_TYPE_CODE);
                    }

                    if (errorTypes != null) {
                        for (Class<? extends Throwable> clazz : errorTypes) {
                            if (clazz.isInstance(error)) {
                                return new Pair<>(error, integer);
                            }
                        }
                    }

                    return new Pair<>(error, UNCHECKED_ERROR_TYPE_CODE);
                })
                .flatMap(errorRetryCountTuple -> {
                    int retryAttempt = errorRetryCountTuple.second;

                    if (retryAttempt == UNCHECKED_ERROR_TYPE_CODE) {
                        return Observable.error(errorRetryCountTuple.first);
                    }
                    long delay = (long) Math.pow(initialDelay, retryAttempt);
                    return Observable.timer(delay, unit);
                });
    }
}
