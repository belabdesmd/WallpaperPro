package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Image;

import java.util.ArrayList;

public interface ExploreContract {

    interface Presenter extends BasePresenter<ExploreContract.View> {

        boolean checkNetwork();

        void initRecyclerView();

        void updateRecyclerView(String mode);

        ArrayList<Image> getRecent();

        ArrayList<Image> getPopular();

        ArrayList<Image> getFeatured();

        String listToString(ArrayList<Image> images);
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Image> images);

        void updateRecyclerView(ArrayList<Image> images);

        void showNoNetwork();

        void showPicturesList();

        void goToWallpaperActivity(int position,ArrayList<Image> images);
    }

}
