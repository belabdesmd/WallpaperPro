package com.devalutix.wallpaperpro.di.modules;

import android.app.Activity;
import android.content.Context;

import com.devalutix.wallpaperpro.di.annotations.ActivityContext;

import dagger.Module;
import dagger.Provides;


@Module
public class ActivityModule {

    //Declarations
    private Activity mActivity;

    //Constructor
    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    /*
        Here we provide the Dependencies
     */

    //Context
    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }
}
