package com.devalutix.wallpaperpro.presenters;

import android.util.Log;

import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;
import com.devalutix.wallpaperpro.utils.Config;

import java.util.ArrayList;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private static String TAG = "FavoritesPresenter";

    //Declarations
    private FavoritesFragment mView;
    private SharedPreferencesHelper mSharedPrefsHelper;

    //Constructor
    public FavoritesPresenter(SharedPreferencesHelper sharedPreferencesHelper) {
        mSharedPrefsHelper = sharedPreferencesHelper;
    }

    //Essential Methods
    @Override
    public void attach(FavoritesContract.View view) {
        mView = (FavoritesFragment) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    //Methods
    @Override
    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: Init Collections List");
        ArrayList<Collection> collections = getCollectionsList();

        //IF There is No Collection (First Time), Create The My Favorites Collection
        if (collections == null) {
            collections = new ArrayList<>();
            collections.add(new Collection(Config.MY_FAVORITES_COLLECTION_NAME, new ArrayList<Image>()));
            mSharedPrefsHelper.saveCollections(collections);
        }

        mView.initRecyclerView(collections);
    }

    @Override
    public void addCollection(Collection collection) {
        Log.d(TAG, "addCollection: Adding Collection");

        ArrayList<Collection> collections = getCollectionsList();
        collections.add(collection);
        mSharedPrefsHelper.saveCollections(collections);

        //Update Recycler View
        mView.updateRecyclerView(collections);
    }

    @Override
    public void editCollection(String collection) {

    }

    @Override
    public void removeCollection() {

    }

    @Override
    public ArrayList<Collection> getCollectionsList() {
        return mSharedPrefsHelper.getCollections();
    }

    @Override
    public void addImageToCollection(String collectionName, Image image) {
        Log.d(TAG, "addImageToCollection: Addign Image To Collection");

        //Get All Collections
        ArrayList<Collection> collections = getCollectionsList();

        boolean found = false;
        int i = 0;

        while (i < collections.size() && !found) {
            if (collections.get(i).getCollectionName().equals(collectionName)) {
                collections.get(i).getCollectionPictures().add(image);
                found = true;
            } else i++;
        }

        mSharedPrefsHelper.saveCollections(collections);
    }
}
