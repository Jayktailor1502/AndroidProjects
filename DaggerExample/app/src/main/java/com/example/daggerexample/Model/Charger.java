package com.example.daggerexample.Model;

import android.util.Log;

import javax.inject.Inject;

public class Charger {

    @Inject
    public Charger() {
        Log.i("Mobile","Charger");
    }

    void setCharger(Mobile mobile){
        Log.i("context"," + =" + mobile);
        Log.i("Mobile","setCharger");
    }
}
