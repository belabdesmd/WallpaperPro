package com.devalutix.wallpaperpro.presenters;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.contracts.ExploreContract;
import com.devalutix.wallpaperpro.pojo.Image;
import com.devalutix.wallpaperpro.ui.fragments.ExploreFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class ExplorePresenter implements ExploreContract.Presenter {
    private static String TAG = "ExplorePresenter";

    //Declarations
    ExploreFragment mView;

    //Constructor
    public ExplorePresenter() {
    }

    //Essential Methods
    @Override
    public void attach(ExploreContract.View view) {
        mView = (ExploreFragment) view;
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
    public void checkNetwork() {

    }

    @Override
    public void initRecyclerView() {

    }

    @Override
    public void updateRecyclerView(String mode) {

    }

    @Override
    public ArrayList<Image> getRecent() {
        return null;
    }

    @Override
    public ArrayList<Image> getPopular() {
        return null;
    }

    @Override
    public ArrayList<Image> getFeatured() {
        return null;
    }
}
