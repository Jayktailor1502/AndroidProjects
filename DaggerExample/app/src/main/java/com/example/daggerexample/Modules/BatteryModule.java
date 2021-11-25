package com.example.daggerexample.Modules;


import com.example.daggerexample.Model.Battery;
import com.example.daggerexample.Model.Cobalt;
import com.example.daggerexample.Model.Lithium;

import dagger.Module;
import dagger.Provides;

@Module
public class BatteryModule {

    @Provides
    Cobalt getCobalt() {
        return new Cobalt();
    }

    @Provides
    Lithium getLithium() {
        Lithium lithium = new Lithium();
        lithium.done();
        return lithium;
    }

    @Provides
    Battery getBattery(Cobalt cobalt, Lithium lithium) {
        return new Battery(cobalt,lithium);
    }
}
