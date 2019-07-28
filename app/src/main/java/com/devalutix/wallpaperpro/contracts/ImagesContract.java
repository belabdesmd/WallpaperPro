package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Wallpaper;

import java.util.ArrayList;

public interface ImagesContract {

    interface Presenter extends BasePresenter<ImagesContract.View> {

        void initRecyclerView(String mode, String name);

        void updateRecyclerView(String name);

        ArrayList<Wallpaper> getImagesFromCollection(String name);

        String listToString(ArrayList<Wallpaper> images);

        String getMode();
    }

    interface View extends BaseView {

        void setToolbar();

        void setPageName(String mode, String name);

        void initRecyclerView(ArrayList<Wallpaper> images);

        void initRetrySheet();

        void updateRecyclerView(ArrayList<Wallpaper> images);

        void goToWallpaperActivity(int position, ArrayList<Wallpaper> images);

        void showNoNetwork();

        void showPicturesList();

        void showEmptyCollection();

        void showRetryCard(String message);

        void hideRetryCard();
    }
}
