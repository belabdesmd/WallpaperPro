package com.devalutix.wallpaperpro.di.components;

import android.content.Context;

import com.devalutix.wallpaperpro.base.BaseApplication;
import com.devalutix.wallpaperpro.di.annotations.ActivityContext;
import com.devalutix.wallpaperpro.di.annotations.ApplicationContext;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.presenters.CategoriesPresenter;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;
import com.devalutix.wallpaperpro.presenters.FavoritesPresenter;
import com.devalutix.wallpaperpro.presenters.ImagesPresenter;
import com.devalutix.wallpaperpro.presenters.MainPresenter;
import com.devalutix.wallpaperpro.presenters.WallpaperPresenter;
import com.devalutix.wallpaperpro.ui.activities.ImagesActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.devalutix.wallpaperpro.utils.PermissionUtil;

import javax.inject.Singleton;

import dagger.Component;

/**********************************
 © 2018 Sam Dev
 ALL RIGHTS RESERVED
 ***********************************/

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);

    /*
        Put Here All the Dependencies The Application Provides
     */

    //Context
    @ApplicationContext
    Context getApplicationContext();
}
