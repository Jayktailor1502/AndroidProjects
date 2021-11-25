package com.example.daggerexample.Model;

import android.util.Log;

import javax.inject.Inject;

public class Processor {

    @Inject
    public Processor() {
        Log.i("Mobile","Processor");
    }
}
