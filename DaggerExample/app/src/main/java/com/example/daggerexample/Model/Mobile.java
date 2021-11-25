package com.example.daggerexample.Model;

import android.util.Log;

import javax.inject.Inject;

public class Mobile {
    private Battery battery;
    private Processor processor;

    //        Constructor Injection
    @Inject
    public Mobile(Battery battery, Processor processor){
        this.battery = battery;
        this.processor = processor;
        Log.i("Mobile","In_Mobile");
    }

    public void run() {
        Log.i("Mobile","Run()");
    }

//    public void fieldInject() {
//        Log.i("Mobile", "fieldInjected");
//    }

//    Method Injection
//    @Inject
//    public void connectCharger(Charger charger){
//        charger.setCharger(this);
//    }


}
