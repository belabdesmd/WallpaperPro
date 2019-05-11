package com.devalutix.wallpaperpro.base;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.devalutix.wallpaperpro.di.components.ApplicationComponent;
import com.devalutix.wallpaperpro.di.components.DaggerApplicationComponent;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;

import androidx.multidex.MultiDexApplication;

import javax.inject.Inject;


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
