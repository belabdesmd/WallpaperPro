package com.devalutix.wallpaperpro.di.components;

import android.content.Context;

import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);

    //Context
    @ApplicationContext
    Context getApplicationContext();
}
