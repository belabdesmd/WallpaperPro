package com.devalutix.wallpaperpro.presenters;

import com.devalutix.wallpaperpro.contracts.CategoriesContract;
import com.devalutix.wallpaperpro.pojo.Category;
import com.devalutix.wallpaperpro.ui.fragments.CategoriesFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class CategoriesPresenter implements CategoriesContract.Presenter {
    private static String TAG = "CategoriesPresenter";

    //Declarations
    CategoriesFragment mView;

    //Constructor
    @Inject
    public CategoriesPresenter() {
    }

    //Essential Methods
    @Override
    public void attach(CategoriesContract.View view) {
        mView = (CategoriesFragment) view;
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
    public ArrayList<Category> getCategories() {
        return null;
    }
}
