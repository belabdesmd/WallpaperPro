package com.devalutix.wallpaperpro.di.components;


import android.content.Context;

import com.devalutix.wallpaperpro.di.annotations.ActivityContext;
import com.devalutix.wallpaperpro.di.modules.ApplicationModule;
import com.devalutix.wallpaperpro.di.modules.MVPModule;
import com.devalutix.wallpaperpro.ui.activities.WallpapersActivity;
import com.devalutix.wallpaperpro.ui.activities.MainActivity;
import com.devalutix.wallpaperpro.ui.activities.WallpaperActivity;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MVPModule.class})
public interface MVPComponent {

    //Inject in Activities
    void inject(MainActivity mainActivity);
    void inject(WallpaperActivity wallpaperActivity);
    void inject(WallpapersActivity wallpapersActivity);

    //Inject in Fragments
    void inject(FavoritesFragment favorites);
    void inject(ExploreFragment explore);
    void inject(CategoriesFragment categories);

    //Context
    @ActivityContext
    Context getActivityContext();
}
