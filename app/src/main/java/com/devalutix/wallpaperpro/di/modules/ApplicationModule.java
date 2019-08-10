package com.devalutix.wallpaperpro.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.Config;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.devalutix.wallpaperpro.utils.PermissionUtil;
import com.google.ads.consent.ConsentForm;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    //Declarations
    private final Application mApplication;
    private ConsentForm form;

    //Constructor
    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

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
    Retrofit providesRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(Config.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiEndpointInterface providesApiEndpoints(Retrofit retrofit){
        return retrofit.create(ApiEndpointInterface.class);
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
    Config providesConfig(){
        SharedPreferences preferences = mApplication.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return new Config(preferences);
    }

    @Provides
    @Singleton
    GDPR providesGDPR(SharedPreferencesHelper sharedPreferencesHelper, Config config){
        return new GDPR(sharedPreferencesHelper, form, mApplication, config);
    }

    @Provides
    @Singleton
    PermissionUtil providesPermissionUtil(SharedPreferencesHelper sharedPreferencesHelper){
        return new PermissionUtil(sharedPreferencesHelper);
    }
}
