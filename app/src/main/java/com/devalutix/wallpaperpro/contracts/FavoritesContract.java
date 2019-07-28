package com.devalutix.wallpaperpro.contracts;

import com.devalutix.wallpaperpro.base.BasePresenter;
import com.devalutix.wallpaperpro.base.BaseView;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;

import java.util.ArrayList;

public interface FavoritesContract {

    interface Presenter extends BasePresenter<FavoritesContract.View> {

        void initRecyclerView();

        void updateRecyclerView();

        void addCollection(Collection collection);

        void editCollection(String oldCollectionName, String collectionName);

        void removeCollection(String collectionName);

        ArrayList<Collection> getCollectionsList();

        void addImageToCollection(String name, Wallpaper image);

    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Collection> collections);

        void initAddCollectionPopUp();

        void initEditCollectionPopUp();

        void updateRecyclerView(ArrayList<Collection> collections);

        void goToImages(String collectionName);

        void showAddCollectionPopUp();

        void hideAddCollectionPopUp();

        void showEditCollectionPopUp(String collectionName);

        void hideEditCollectionPopUp();

        void enableDoneButton();

        void disableDoneButton();
    }

}
