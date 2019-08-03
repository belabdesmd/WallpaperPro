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

        void savePicture(Wallpaper wallpaper, int position);

        void sharePicture(Wallpaper wallpaper);

        void setAsWallpaper(Wallpaper wallpaper);

        void loadInterstitialAd(InterstitialAd mInterstitialAd);

        void loadAd(AdView ad);

        void openAddToFavoritesPopUp();

        void setWallpapers(String wallpapers);

        ArrayList<Wallpaper> getWallpapers();

        Wallpaper getWallpapers(int currentItem);

        ArrayList<Collection> getCollections();
    }

    interface View extends BaseView {

        void setToolbar();

        void initViewPager();

        void initPopUpInfos();

        void initInfos(int position);

        void initPopUpFavorite();

        void initInterstitialAd();

        void initAdBanner();

        void showInfos();

        void showFavorite();

        void showInterstitialAd();

        void hideInfos();

        void hideFavorite();
    }

}
