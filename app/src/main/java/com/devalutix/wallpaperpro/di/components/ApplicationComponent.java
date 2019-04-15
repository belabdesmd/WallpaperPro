package com.devalutix.wallpaperpro.di.components;

import android.content.Context;

import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.di.annotations.ActivityContext;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.di.modules.ActivityModule;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**********************************
 © 2018 Sam Dev
 ALL RIGHTS RESERVED
 ***********************************/

@Singleton
@Component(modules = {ApplicationModule.class, ActivityModule.class})
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);
    void inject(MainActivity mainActivity);

    /*
        Put Here All the Dependencies The Application Provides
     */

    //Context
    @ApplicationContext
    Context getApplicationContext();
    @ActivityContext
    Context getActivityContext();

    //Models

    //Presenters

    //Utils
}
