package com.devalutix.wallpaperpro.base;

import android.content.Context;

import com.devalutix.wallpaperpro.di.components.ApplicationComponent;
import com.devalutix.wallpaperpro.di.components.DaggerApplicationComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;

import androidx.multidex.MultiDexApplication;



public class BaseApplication extends MultiDexApplication {
    ApplicationComponent appComponent;

    public ApplicationComponent getComponent() {
        return appComponent;
    }

    public static BaseApplication getBaseApplication(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Dagger For Application
        appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //Inject the Component Here
        appComponent.inject(this);
    }
}
