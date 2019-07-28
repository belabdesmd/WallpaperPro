package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public interface WallpaperContract {

    interface Presenter extends BasePresenter<WallpaperContract.View> {

        void savePicture(Wallpaper image, int position);

        void openAddToFavoritesPopUp();

        void sharePicture(Wallpaper image);

        void setAsWallpaper(Wallpaper image);

        void setImages(String images);

        ArrayList<Wallpaper> getImages();

        ArrayList<Collection> getCollections();

        void loadInterstitialAd(InterstitialAd mInterstitialAd);

        void loadAd(AdView ad);

        Wallpaper getImage(int currentItem);
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
