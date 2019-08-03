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

        void addWallpaperToCollection(String name, Wallpaper wallpaper);

        ArrayList<Collection> getCollectionsList();
    }

    interface View extends BaseView {

        void initRecyclerView(ArrayList<Collection> collections);

        void updateRecyclerView(ArrayList<Collection> collections);

        void initAddCollectionPopUp();

        void initEditCollectionPopUp();

        void showAddCollectionPopUp();

        void showEditCollectionPopUp(String collectionName);

        void hideAddCollectionPopUp();

        void hideEditCollectionPopUp();

        void enableDoneButton();

        void disableDoneButton();

        void goToWallpapers(String collectionName);
    }

}
