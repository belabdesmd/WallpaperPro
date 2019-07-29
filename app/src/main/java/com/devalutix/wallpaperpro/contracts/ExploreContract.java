package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Wallpaper;

import java.util.ArrayList;

public interface ExploreContract {

    interface Presenter extends BasePresenter<ExploreContract.View> {

        void initRecyclerView();

        void updateRecyclerView(String mode);

        String listToString(ArrayList<Wallpaper> images);

        String getMode();
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Wallpaper> images);

        void updateRecyclerView(ArrayList<Wallpaper> images);

        void initRetrySheet();

        void showNoNetwork();

        void showPicturesList();

        void showEmptyCollection(String message);

        void showRetryCard(String message);

        void hideRetryCard();

        void enableFilter(int position);

        void disableAllFilters();

        void goToWallpaperActivity(int position,ArrayList<Wallpaper> images);

        void refresh();
    }

}
