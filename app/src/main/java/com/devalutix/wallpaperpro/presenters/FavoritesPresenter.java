package com.devalutix.wallpaperpro.presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.devalutix.wallpaperpro.R;
import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.models.SharedPreferencesHelper;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.pojo.Wallpaper;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;
import com.devalutix.wallpaperpro.utils.Config;

import java.util.ArrayList;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    /***************************************** Declarations ***************************************/
    private FavoritesFragment mView;
    private SharedPreferencesHelper mSharedPrefsHelper;

    /***************************************** Constructor ****************************************/
    public FavoritesPresenter(SharedPreferencesHelper sharedPreferencesHelper) {
        mSharedPrefsHelper = sharedPreferencesHelper;
    }

    /***************************************** Essential Methods **********************************/
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

    /***************************************** Methods ********************************************/
    @Override
    public void initRecyclerView() {
        ArrayList<Collection> collections = getCollectionsList();

        //IF There is No Collection (First Time), Create The My Favorites Collection
        if (collections == null) {
            collections = new ArrayList<>();
            collections.add(new Collection(mView.getResources().getString(R.string.MyFavorites), new ArrayList<>()));
            mSharedPrefsHelper.saveCollections(collections);
        }

        mView.initRecyclerView(collections);
    }

    @Override
    public void updateRecyclerView() {
        ArrayList<Collection> collections = getCollectionsList();

        mView.updateRecyclerView(collections);
    }

    @Override
    public ArrayList<Collection> getCollectionsList() {
        return mSharedPrefsHelper.getCollections();
    }

    @Override
    public void addCollection(Collection collection) {

        ArrayList<Collection> collections = getCollectionsList();
        if (!collections.contains(collection))
            collections.add(collection);
        else
            Toast.makeText(mView.getActivity(), mView.getResources().getString(R.string.collection_exists), Toast.LENGTH_SHORT).show();
        mSharedPrefsHelper.saveCollections(collections);

        //Update Recycler View
        mView.updateRecyclerView(collections);
    }

    @Override
    public void editCollection(String oldCollectionName, String collectionName) {

        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();

        boolean found = false;
        int i = 0;

        while (!found && i < allCollections.size()) {
            if (allCollections.get(i).getCollectionName().equals(oldCollectionName)) {
                allCollections.get(i).setCollectionName(collectionName);
                found = true;
            } else i++;
        }

        mSharedPrefsHelper.saveCollections(allCollections);
    }

    @Override
    public void removeCollection(String collectionName) {

        ArrayList<Collection> allCollections = mSharedPrefsHelper.getCollections();

        //Remove Collection From List
        if (!collectionName.equals(mView.getResources().getString(R.string.MyFavorites)))
            allCollections.remove(new Collection(collectionName, null));

        //Save List
        mSharedPrefsHelper.saveCollections(allCollections);
    }

    @Override
    public void add_removeWallpaper(Context context, String collectionName, Wallpaper wallpaper) {

        //Get All Collections
        ArrayList<Collection> collections = getCollectionsList();

        boolean found = false;
        int i = 0;

        //If Wallpaper Already Exists, Remove it from Collection
        //Else: Add It
        while (i < collections.size() && !found) {
            if (collections.get(i).getCollectionName().equals(collectionName)) {
                if (!collections.get(i).getCollectionWallpapers().contains(wallpaper)) {
                    collections.get(i).getCollectionWallpapers().add(wallpaper);
                    if (!collectionName.equals(context.getResources().getString(R.string.MyFavorites)))
                        Toast.makeText(context, context.getResources().getString(R.string.wallpaper_added_msg) +
                                " " + collectionName, Toast.LENGTH_SHORT).show();
                } else if (!collectionName.equals(context.getResources().getString(R.string.MyFavorites))) {
                    collections.get(i).getCollectionWallpapers().remove(wallpaper);
                    Toast.makeText(context, context.getResources().getString(R.string.wallpaper_removed_msg) +
                            " " + collectionName, Toast.LENGTH_SHORT).show();
                }
                found = true;
            } else i++;
        }

        //Save Collections
        mSharedPrefsHelper.saveCollections(collections);
    }
}
