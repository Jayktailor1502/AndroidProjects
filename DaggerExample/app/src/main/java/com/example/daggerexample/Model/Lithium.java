package com.example.daggerexample.Model;

import android.util.Log;

public class Lithium {

    //    lets suppose, we dont own this class so we can't annotate it with @Inject

    public Lithium() {
        Log.i("Mobile", "Lithium");
    }

    public void done() {
        Log.i("Mobile", "done:Lithium");
    }
}
