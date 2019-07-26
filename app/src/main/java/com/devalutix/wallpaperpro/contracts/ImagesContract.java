package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Image;

import java.util.ArrayList;

public interface ImagesContract {

    interface Presenter extends BasePresenter<ImagesContract.View> {

        boolean hasInternetNetwork();

        void initRecyclerView(String mode, String name);

        void updateRecyclerView(String name);

        ArrayList<Image> getImagesFromCollection(String name);

        ArrayList<Image> getImageFromCategory(String name);

        ArrayList<Image> getSearchResults(String name);

        String listToString(ArrayList<Image> images);

        String getThumbnail(String mode, String name);

        String getMode();
    }

    interface View extends BaseView {

        void setToolbar();

        void setPageName(String mode, String name);

        void initRecyclerView(ArrayList<Image> images);

        void initRetrySheet();

        void updateRecyclerView(ArrayList<Image> images);

        void goToWallpaperActivity(int position, ArrayList<Image> images);

        void showNoNetwork();

        void showPicturesList();

        void showRetryCard(String message);

        void hideRetryCard();
    }
}
