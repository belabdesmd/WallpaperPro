package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.google.android.gms.ads.AdView;

public interface MainContract {

    interface Presenter extends BasePresenter<MainContract.View> {

        void requestPermission(String writeExternalStorage, int requestWriteStorage);

        void checkGDPRConsent();

        void loadAd(AdView ad);

        void searchWallpapers(String query);

        void enableDarkMode(boolean isChecked);

        boolean isDarkModeEnabled();

        void grantDownload();

        void disableDownload();

        void showDrawerMenuIndex();
    }

    interface View extends BaseView {

        void initViewPager();

        void initTabLayout();

        void initAdBanner();

        void initSearchBar();

        void darkModeListener();

        void showSearchBar();

        void showAddCollection();

        void hideAll();

        void enableTabAt(int i);

        void hideKeyboard();

        void restartApp();
    }

}
