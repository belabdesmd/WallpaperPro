package com.devalutix.wallpaperpro.presenters;

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
        ArrayList<Collection> collections = getCollectionsList();

        if (collections == null) {
            collections = new ArrayList<>();
            collections.add(new Collection(Config.MY_FAVORITES_COLLECTION_NAME, new ArrayList<Image>()));
            mSharedPrefsHelper.saveCollections(collections);
        }

        mView.initRecyclerView(collections);
    }

    @Override
    public void addCollection(Collection collection) {
        ArrayList<Collection> collections = getCollectionsList();
        collections.add(collection);
        mSharedPrefsHelper.saveCollections(collections);
        mView.updateRecyclerView(collections);
    }

    @Override
    public ArrayList<Collection> getCollectionsList() {
        return mSharedPrefsHelper.getCollections();
    }

    @Override
    public void addToCollection(String name, Image image) {
        ArrayList<Collection> collections = getCollectionsList();
        for (Collection collection :
                collections) {
            if (collection.getCollectioName().equals(name))
                collection.getCollectionPictures().add(image);
        }

        mSharedPrefsHelper.saveCollections(collections);
    }
}
