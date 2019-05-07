package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Image;

import java.util.ArrayList;

public interface WallpaperContract {

    interface Presenter extends BasePresenter<WallpaperContract.View> {

        void savePicture(String url);

        void likePicture();

        void dislikePicture();

        void addToFavorites();

        void addRating(Image image);

        void sharePicture();

        ArrayList<Image> getImages(String images);
    }

    interface View extends BaseView {

        void setToolbar();

        void initViewPager();

        void initPopUpInfos();

        void initInfos(int position);

        void initPopUpFavorite();

        void initInterstitialAd();

        void showInfos();

        void hideInfos();

        void showFavorite();

        void hideFavorite();

        void showInterstitialAd();
    }

}
