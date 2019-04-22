package com.devalutix.wallpaperpro.base;

import com.devalutix.wallpaperpro.di.components.ApplicationComponent;
import com.devalutix.wallpaperpro.di.components.DaggerApplicationComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;

import androidx.multidex.MultiDexApplication;


public class BaseApplication extends MultiDexApplication {
    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Dagger For Application
        appComponent = getComponent();

        //Inject the Component Here
        appComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return appComponent;
    }
}
