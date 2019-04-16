package com.devalutix.wallpaperpro.presenters;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.contracts.FavoritesContract;
import com.devalutix.wallpaperpro.pojo.Collection;
import com.devalutix.wallpaperpro.ui.fragments.FavoritesFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private static String TAG = "FavoritesPresenter";

    //Declarations
    FavoritesFragment mView;

    //Constructor
    @Inject
    public FavoritesPresenter() {
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

    }

    @Override
    public void addCollection(Collection collection) {

    }

    @Override
    public ArrayList<Collection> getCollectionsList() {
        return null;
    }
}
