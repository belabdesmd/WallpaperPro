package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.utils.GDPR;
import com.google.android.gms.ads.AdView;

public interface MainContract {

    interface Presenter extends BasePresenter<MainContract.View> {

        void requestPermission(String writeExternalStorage, int requestWriteStorage);

        void initGDPR(AdView ad);

        GDPR getGDPR();

        void enableDarkMode(boolean isChecked);

        boolean isDarkModeEnabled();
    }

    interface View extends BaseView {

        void initAdBanner();

        void initSearchBar();

        void darkModeListener();

        void showSearchBar();

        void showAddCollection();

        void hideAll();

        void enableTabAt(int i);

        void initViewPager();

        void initTabLayout();

        void hideKeyboard();

        void restartApp();
    }

}
