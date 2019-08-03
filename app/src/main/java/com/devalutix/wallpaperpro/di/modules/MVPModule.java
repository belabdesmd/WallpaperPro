package com.devalutix.wallpaperpro.di.modules;

import android.app.Activity;
import android.content.Context;

import com.devalutix.wallpaperpro.di.annotations.ActivityContext;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.presenters.WallpapersPresenter;
import com.devalutix.wallpaperpro.presenters.MainPresenter;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.utils.ApiEndpointInterface;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.devalutix.wallpaperpro.utils.PermissionUtil;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MVPModule {

    private Activity mActivity;

    //Constructor
    public MVPModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

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


    @Provides
    @Singleton
    MainPresenter providesMainPresenter(SharedPreferencesHelper sharedPreferencesHelper,
                                        PermissionUtil util,
                                        GDPR gdpr) {
        return new MainPresenter(util, sharedPreferencesHelper, gdpr);
    }

    @Provides
    @Singleton
    WallpapersPresenter providesImagesPresenter(Gson gson, SharedPreferencesHelper mSharedPrefsHelper,
                                                ApiEndpointInterface apiService) {
        return new WallpapersPresenter(gson, mSharedPrefsHelper, apiService);
    }

    @Provides
    @Singleton
    WallpaperPresenter providesWallpaperPresenter(Gson gson, SharedPreferencesHelper mSharedPrefsHelper, GDPR gdpr, ApiEndpointInterface apiService) {
        return new WallpaperPresenter(gson, mSharedPrefsHelper, gdpr, apiService);
    }

    @Provides
    @Singleton
    ExplorePresenter providesExplorePresenter(Gson gson, ApiEndpointInterface apiService) {
        return new ExplorePresenter(gson, apiService);
    }

    @Provides
    @Singleton
    CategoriesPresenter providesCategoriesPresenter(ApiEndpointInterface apiService) {
        return new CategoriesPresenter(apiService);
    }

    @Provides
    @Singleton
    FavoritesPresenter providesFavoritesPresenter(SharedPreferencesHelper sharedPreferencesHelper) {
        return new FavoritesPresenter(sharedPreferencesHelper);
    }
}
