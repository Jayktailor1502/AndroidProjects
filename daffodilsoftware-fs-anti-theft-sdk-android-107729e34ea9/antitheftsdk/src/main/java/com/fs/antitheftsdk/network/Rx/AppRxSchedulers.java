package com.fs.antitheftsdk.network.Rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Provides different threads for Rx Java operations.
 */
public class AppRxSchedulers implements RxSchedulers {

    /**
     * Provides Android main thread scheduler
     * Used to perform foreground/ui operations
     *
     * @return {@link AndroidSchedulers#mainThread()}
     */
    @Override
    public Scheduler androidUI() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * Provides input/output thread scheduler
     * Used to perform background operation
     *
     * @return {@link Schedulers#io()}
     */
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    /**
     * Provides computation thread scheduler
     * Used for light weight background work such as event-loops and callback processing
     *
     * @return {@link Schedulers#computation()}
     */
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    /**
     * Provides current (same thread in which function is created) thread scheduler
     * This is useful in unit testing
     *
     * @return {@link Schedulers#trampoline()}
     */
    @Override
    public Scheduler immediate() {
        return Schedulers.trampoline();
    }


    /**
     * Provides observable for Rx function to subscribe
     * Calling this function will make your observable to to work in io thread that is background thread
     * and observe on android main thread.
     *
     * @return ObservableTransformer object which can be used with compose
     * @see #io()
     * @see #androidUI()
     */
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(io())
                .observeOn(androidUI());
    }
}