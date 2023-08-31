package com.fs.antitheftsdk.network.Rx;

import io.reactivex.Scheduler;


public interface RxSchedulers {

    Scheduler androidUI();

    Scheduler io();

    Scheduler computation();

    Scheduler immediate();
}