package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.presenters.ExplorePresenter;

import java.util.ArrayList;

public interface ExploreContract {

    interface Presenter extends BasePresenter<ExploreContract.View> {

        void initRecyclerView();

        void updateRecyclerView(String mode);

        String listToString(ArrayList<Wallpaper> wallpapers);

        String getMode();

        void goToWallpaperActivity(int position,ArrayList<Wallpaper> wallpapers);
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Wallpaper> wallpapers);

        void updateRecyclerView(ArrayList<Wallpaper> wallpapers);

        void initRetrySheet();

        void showNoNetwork();

        void showWallpapersList();

        void showEmptyCollection();

        void showRetryCard(String message);

        void hideRetryCard();

        void enableFilter(int position);

        void disableAllFilters();

        void refresh();

        boolean isRefreshing();

        ExplorePresenter getPresenter();
    }

}
