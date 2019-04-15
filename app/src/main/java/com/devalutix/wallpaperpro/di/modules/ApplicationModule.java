package com.devalutix.wallpaperpro.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.google.ads.consent.ConsentForm;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**********************************
 © 2018 Sam Dev
 ALL RIGHTS RESERVED
 ***********************************/

@Module
public class ApplicationModule {

    //Declarations
    private final Application mApplication;
    private ConsentForm form;

    //Constructor
    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    /*
        Here We Provide The Dependencies
     */

    //Context
    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


    /*
        Models
     */

    @Provides
    Gson provideGson(){
        return new Gson();
    }

    @Provides
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("BASICS", Context.MODE_PRIVATE);
    }

    /*
        Utils
     */
}
