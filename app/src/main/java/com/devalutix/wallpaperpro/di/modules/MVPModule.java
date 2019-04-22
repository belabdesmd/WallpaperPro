package com.devalutix.wallpaperpro.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.devalutix.wallpaperpro.di.annotations.ActivityContext;
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
                                        GDPR gdpr){
        return new MainPresenter(util,sharedPreferencesHelper,gdpr);
    }

    @Provides
    @Singleton
    ImagesPresenter providesImagesPresenter(){
        return new ImagesPresenter();
    }

    @Provides
    @Singleton
    WallpaperPresenter providesWallpaperPresenter(){
        return new WallpaperPresenter();
    }

    @Provides
    @Singleton
    ExplorePresenter providesExplorePresenter(){
        return new ExplorePresenter();
    }

    @Provides
    @Singleton
    CategoriesPresenter providesCategoriesPresenter(){
        return new CategoriesPresenter();
    }

    @Provides
    @Singleton
    FavoritesPresenter providesFavoritesPresenter(){
        return new FavoritesPresenter();
    }
}
