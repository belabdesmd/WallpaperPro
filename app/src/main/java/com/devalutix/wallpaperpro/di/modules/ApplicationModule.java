package com.devalutix.wallpaperpro.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.presenters.ImagesPresenter;
import com.devalutix.wallpaperpro.presenters.MainPresenter;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.devalutix.wallpaperpro.utils.PermissionUtil;
import com.google.ads.consent.ConsentForm;
import com.google.gson.Gson;

import javax.inject.Singleton;

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
    @Singleton
    Gson provideGson(){
        return new Gson();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("BASICS", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPrefsHelper(SharedPreferences sharedPreferences, Gson gson){
        return new SharedPreferencesHelper(sharedPreferences,gson);
    }

    /*
        Utils
     */
    @Provides
    @Singleton
    GDPR providesGDPR(){
        return new GDPR(form, mApplication);
    }

    @Provides
    @Singleton
    PermissionUtil providesPermissionUtil(SharedPreferencesHelper sharedPreferencesHelper){
        return new PermissionUtil(sharedPreferencesHelper);
    }
}
