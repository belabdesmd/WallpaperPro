package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Collection;

import java.util.ArrayList;

public interface FavoritesContract {

    interface Presenter extends BasePresenter<FavoritesContract.View> {

        void initRecyclerView();

        void addCollection(Collection collection);

        ArrayList<Collection> getCollectionsList();
    }

    interface View extends BaseView {

        void initPopUpWindow();

        void initRecyclerView(ArrayList<Collection> collections);

        void updateRecyclerView(ArrayList<Collection> collections);

        void showPopUpWindow();

        void hidePopUpWindow();

        void goToImages(String collectionName);

    }

}
