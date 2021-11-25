package com.example.daggerexample.Component;

import com.example.daggerexample.MainActivity;
import com.example.daggerexample.Model.Battery;
import com.example.daggerexample.Model.Mobile;
import com.example.daggerexample.Modules.BatteryModule;

import dagger.Component;
import dagger.Module;

@Component(modules = BatteryModule.class)
public interface MobileComponent {

//    Provision Method
    Mobile getMobile();

//    Field Injection
    void inject(MainActivity activity);

}
