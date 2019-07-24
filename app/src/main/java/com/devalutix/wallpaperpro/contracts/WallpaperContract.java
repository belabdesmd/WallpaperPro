package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public interface WallpaperContract {

    interface Presenter extends BasePresenter<WallpaperContract.View> {

        void savePicture(Image image);

        void openAddToFavoritesPopUp();

        void sharePicture(Image image);

        void setAsWallpaper(Image image);

        ArrayList<Image> getImages(String images);

        ArrayList<Collection> getCollections();

        void loadInterstitialAd(InterstitialAd mInterstitialAd);

        void loadAd(AdView ad);
    }

    interface View extends BaseView {

        void initAdBanner();

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
