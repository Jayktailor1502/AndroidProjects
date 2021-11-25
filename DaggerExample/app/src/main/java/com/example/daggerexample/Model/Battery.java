package com.example.daggerexample.Model;

import android.util.Log;

import javax.inject.Inject;

public class Battery {

//    lets suppose, we dont own this class so we can't annotate it with @Inject

    private Cobalt cobalt;
    private Lithium lithium;

    public Battery(Cobalt cobalt, Lithium lithium) {
        this.cobalt=cobalt;
        this.lithium=lithium;
        Log.i("Mobile", "Battery");
    }
}
