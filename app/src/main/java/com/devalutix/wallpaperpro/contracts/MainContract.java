package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;

public interface MainContract {

    interface Presenter extends BasePresenter<MainContract.View> {

        void requestPermission();

        void initGDPR();
    }

    interface View extends BaseView {

        void initAdBanner();

        void showSearchBar();

        void showAddCollection();

        void initViewPager();

        void initTabLayout();
    }

}
