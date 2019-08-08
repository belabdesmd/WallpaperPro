package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Wallpaper;

import java.util.ArrayList;

public interface WallpapersContract {

    interface Presenter extends BasePresenter<WallpapersContract.View> {

        void initRecyclerView(String mode, String name);

        void updateRecyclerView(String name);

        ArrayList<Wallpaper> getWallpapersFromCollection(String name);

        String listToString(ArrayList<Wallpaper> wallpapers);

        String getMode();
    }

    interface View extends BaseView {

        void setToolbar();

        void setPageName(String name);

        void initRecyclerView(ArrayList<Wallpaper> wallpapers);

        void updateRecyclerView(ArrayList<Wallpaper> wallpapers);

        void initRetrySheet();

        void showNoNetwork();

        void showPicturesList();

        void showEmptyCollection(String message);

        void showRetryCard(String message);

        void hideRetryCard();

        void refresh();

        void goToWallpaperActivity(int position, ArrayList<Wallpaper> wallpapers);
    }
}
