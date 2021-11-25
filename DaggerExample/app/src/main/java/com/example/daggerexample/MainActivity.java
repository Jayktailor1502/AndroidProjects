package com.example.daggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.daggerexample.Component.DaggerMobileComponent;
import com.example.daggerexample.Component.MobileComponent;
import com.example.daggerexample.Model.Mobile;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

//    Field Injection
    @Inject
    Mobile mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Constructor Injection
        MobileComponent mobileComponent = DaggerMobileComponent.create();
        Mobile mobile = mobileComponent.getMobile();
        mobile.run();

//        Field Injection
//        MobileComponent mobileComponent = DaggerMobileComponent.create();
//        mobileComponent.inject(this);
//        mobile.run();
    }
}