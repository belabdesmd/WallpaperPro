package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;

import java.util.ArrayList;

public interface FavoritesContract {

    interface Presenter extends BasePresenter<FavoritesContract.View> {

        void initRecyclerView();

        void addCollection(Collection collection);

        ArrayList<Collection> getCollectionsList();

        void addToCollection(String name, Image image);
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Collection> collections);

        void initAddCollectionPopUp();

        void updateRecyclerView(ArrayList<Collection> collections);

        void goToImages(String collectionName);

        void showAddCollectionPopUp();

        void hideAddCollectionPopUp();

        void enableDoneButton();

        void disableDoneButton();
    }

}
